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

                    List<SchoolTest> allTests = getNewTests(sephirInterface);

                    Map<String, SchoolTest> knownTests = convertTestsToMap(getOldTests(persister));

                    Map<String, List<Duration>> alreadySentReminders = getAlreadySentReminders(persister);
                    Map<String, List<Duration>> newlySentReminders = new HashMap<>();

                    for (SchoolTest test : allTests) {
                        SchoolTest knownTest = knownTests.get(test.getId());

                        if (knownTest == null) {
                            notificationSender.sendNewAnnouncedTestNotification(test);
                        } else if (hasDateChanged(test, knownTest)) {
                            notificationSender.sendUpdatedAnnouncedTestNotification(test, knownTest);

                        }

                        if (isNewMark(test, knownTest) || hasMarkChanged(test, knownTest)) {
                            notificationSender.sendNewMarkNotification(test, test.getAverageMark(sephirInterface));
                        }

                        if (settings.sendReminders()) {
                            LocalDateTime testDate = test.getDate().toLocalDateTime(new LocalTime(0, 0, 0));
                            if (LocalDateTime.now().isBefore(testDate)) {
                                List<Duration> alreadyRemindedDurations = getTestSpecificAlreadySentReminders(alreadySentReminders, test);
                                for (Duration reminderDuration : settings.getTestReminders()) {
                                    if (!alreadyRemindedDurations.contains(reminderDuration)) {
                                        if (mustReminderBeSent(testDate, reminderDuration)) {
                                            notificationSender.sendReminder(test);
                                            alreadyRemindedDurations.add(reminderDuration);
                                        }
                                    }
                                }
                                newlySentReminders.put(test.getId(), alreadyRemindedDurations);
                            }
                        }
                    }
                    persister.persist(TESTS_FILE_NAME, allTests);
                    persister.persist(REMINDER_NOTIFICATION_FILE_NAME, newlySentReminders);
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
                    cancelScheduling(getApplicationContext());
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

    @NonNull
    private static NotifierSettings getNotifierSettings() {
        return new NotifierSettings();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public static void scheduleJob(Context applicationContext, int id) {
        JobScheduler jobScheduler = getJobScheduler(applicationContext);
        ComponentName serviceComponent = new ComponentName(applicationContext, NotifierService.class);
        JobInfo.Builder builder = new JobInfo.Builder(id, serviceComponent);
        builder.setMinimumLatency(getNotifierSettings().getLatency());
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        jobScheduler.schedule(builder.build());
    }

    private static JobScheduler getJobScheduler(Context applicationContext) {
        return (JobScheduler) applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public static void cancelScheduling(Context applicationContext) {
        JobScheduler jobScheduler = getJobScheduler(applicationContext);
        jobScheduler.cancelAll();
    }
}
