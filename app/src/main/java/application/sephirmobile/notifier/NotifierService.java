package application.sephirmobile.notifier;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.sephirmobile.login.Login;
import application.sephirmobile.login.LoginUtils;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class NotifierService extends JobService {

    private static final String REMINDER_NOTIFICATION_FILE_NAME = "reminders.ser";
    private static final String TESTS_FILE_NAME = "tests.ser";

    @Override
    public boolean onStartJob(JobParameters params) {
        new Thread(() -> {
            boolean reschedule = true;
            final NotifierSettings settings = getNotifierSettings();
            NotificationSender notificationSender = new NotificationSender(this, settings);
            try {
                Login login = LoginUtils.load();
                SephirInterface sephirInterface = SephirInterface.newSephirInterface();
                if (login != null && sephirInterface.login(login)) {
                    Persister persister = new Persister(NotifierService.this);

                    List<SchoolTest> newTests = getNewTests(sephirInterface);

                    List<SchoolTest> oldTests = getOldTests(persister);
                    Map<String, SchoolTest> oldTestMap = convertTestsToMap(oldTests);

                    Map<String, List<Duration>> reminderMap = getAlreadySentReminders(persister);
                    Map<String, List<Duration>> newReminderMap = new HashMap<>();

                    for (SchoolTest newTest : newTests) {
                        SchoolTest oldTest = oldTestMap.get(newTest.getId());

                        if (oldTest == null) {
                            notificationSender.sendNewAnnouncedTestNotification(newTest);
                        } else {
                            if (hasDateChanged(newTest, oldTest)) {
                                notificationSender.sendUpdatedAnnouncedTestNotification(newTest, oldTest);
                            }
                        }

                        if (isNewMark(newTest, oldTest) || hasMarkChanged(newTest, oldTest)) {
                            notificationSender.sendNewMarkNotification(newTest, newTest.getAverageMark(sephirInterface));
                        }

                        if (settings.sendReminders()) {
                            LocalDateTime testDate = newTest.getDate().toLocalDateTime(new LocalTime(0, 0, 0));
                            if (LocalDateTime.now().isBefore(testDate)) {
                                List<Duration> alreadyRemindedDurations = getTestSpecificAlreadySentReminders(reminderMap, newTest);
                                for (Duration reminderDuration : settings.getTestReminders()) {
                                    if (!alreadyRemindedDurations.contains(reminderDuration)) {
                                        if (mustReminderBeSent(testDate, reminderDuration)) {
                                            notificationSender.sendReminder(newTest);
                                            alreadyRemindedDurations.add(reminderDuration);
                                        }
                                    }
                                }
                                newReminderMap.put(newTest.getId(), alreadyRemindedDurations);
                            }
                        }
                    }
                    persister.persist(TESTS_FILE_NAME, newTests);
                    persister.persist(REMINDER_NOTIFICATION_FILE_NAME, newReminderMap);
                } else {
                    reschedule = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                notificationSender.sendExceptionMessage(e);
            } finally {
                if (reschedule) {
                    NotifierService.scheduleJob(getApplicationContext(), 0);
                } else {
                    notificationSender.sendLoginError();
                }
            }
        }).start();
        return true;
    }

    private boolean mustReminderBeSent(LocalDateTime testDate, Duration reminderDuration) {
        return Minutes.minutesBetween(LocalDateTime.now(), testDate).getMinutes() < reminderDuration.getStandardMinutes();
    }


    @NonNull
    private List<Duration> getTestSpecificAlreadySentReminders(Map<String, List<Duration>> reminderMap, SchoolTest test) {
        List<Duration> remindedDurations = reminderMap.get(test.getId());
        if (remindedDurations == null) {
            remindedDurations = new ArrayList<>();
        }
        return remindedDurations;
    }


    private boolean isNewMark(SchoolTest newTest, SchoolTest oldTest) {
        return oldTest == null && newTest.getMark() != 0.0;
    }

    private boolean hasMarkChanged(SchoolTest newTest, SchoolTest oldTest) {
        return oldTest != null && oldTest.getMark() != newTest.getMark();
    }


    private boolean hasDateChanged(SchoolTest newTest, SchoolTest oldTest) {
        return !oldTest.getDate().equals(newTest.getDate());
    }


    @NonNull
    private Map<String, SchoolTest> convertTestsToMap(List<SchoolTest> oldTests) {
        Map<String, SchoolTest> oldTestMap = new HashMap<>();
        for (SchoolTest test : oldTests) {
            oldTestMap.put(test.getId(), test);
        }
        return oldTestMap;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private Map<String, List<Duration>> getAlreadySentReminders(Persister persister) throws IOException, ClassNotFoundException {
        Map<String, List<Duration>> reminderMap = (Map<String, List<Duration>>) persister.load(REMINDER_NOTIFICATION_FILE_NAME);
        if (reminderMap == null) {
            reminderMap = new HashMap<>();
        }
        return reminderMap;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private List<SchoolTest> getOldTests(Persister persister) throws IOException, ClassNotFoundException {
        List<SchoolTest> oldTests = (List<SchoolTest>) persister.load(TESTS_FILE_NAME);
        if (oldTests == null) {
            oldTests = new ArrayList<>();
        }
        return oldTests;
    }

    @NonNull
    private List<SchoolTest> getNewTests(SephirInterface sephirInterface) throws IOException {
        List<SchoolClass> schoolClasses = new SchoolClassGetter(sephirInterface).get();
        SchoolTestGetter schoolTestGetter = new SchoolTestGetter(sephirInterface);
        List<SchoolTest> newTests = new ArrayList<>();
        for (SchoolClass schoolClass : schoolClasses) {
            newTests.addAll(schoolTestGetter.get(schoolClass));
        }
        return newTests;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @NonNull
    private static NotifierSettings getNotifierSettings() {
        return new NotifierSettings();
    }

    public static void scheduleJob(Context context, int id) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName serviceComponent = new ComponentName(context, NotifierService.class);
        JobInfo.Builder builder = new JobInfo.Builder(id, serviceComponent);
        builder.setMinimumLatency(getNotifierSettings().getLatency());
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        jobScheduler.schedule(builder.build());
    }
}
