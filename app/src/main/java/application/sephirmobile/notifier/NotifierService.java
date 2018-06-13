package application.sephirmobile.notifier;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.sephirmobile.R;
import application.sephirmobile.login.LoginUtils;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class NotifierService extends JobService {

    public static final String CHANNEL_ID = "default";
    private static int counter = 0;
    private NotificationManager notificationManager;
    //TODO from settings
    private static final NotifierSettings settings = new NotifierSettings();
    private static final String REMINDER_NOTIFICATION_FILE_NAME = "reminders.ser";
    private static final String TESTS_FILE_NAME = "tests.ser";
    private static final String PATTERN = "dd.MM.yyyy";

    @Override
    @SuppressWarnings("unchecked")
    public boolean onStartJob(JobParameters params) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        new Thread(() -> {
            boolean reschedule = true;
            try {
                SephirInterface sephirInterface = new SephirInterface();
                reschedule = sephirInterface.login(LoginUtils.load());
                List<SchoolClass> schoolClasses = new SchoolClassGetter(sephirInterface).get();
                SchoolTestGetter schoolTestGetter = new SchoolTestGetter(sephirInterface);
                List<SchoolTest> newTests = new ArrayList<>();
                for (SchoolClass schoolClass : schoolClasses) {
                    newTests.addAll(schoolTestGetter.get(schoolClass));
                }

                Persister persister = new Persister(NotifierService.this);
                List<SchoolTest> oldTests = (List<SchoolTest>) persister.load(TESTS_FILE_NAME);
                Map<String, List<Duration>> reminderMap = (Map<String, List<Duration>>) persister.load(REMINDER_NOTIFICATION_FILE_NAME);
                if (reminderMap == null) {
                    reminderMap = new HashMap<>();
                }

                //TODO testing
                if (oldTests != null) {
                    Map<String, SchoolTest> oldTestMap = new HashMap<>();
                    for (SchoolTest test : oldTests) {
                        oldTestMap.put(test.getId(), test);
                    }
                    for (SchoolTest newTest : newTests) {
                        SchoolTest oldTest = oldTestMap.get(newTest.getId());
                        if (oldTest == null) {
                            if (settings.isNewAnnouncedTest()) {
                                sendNotification("New Test", newTest.getDate().toString(PATTERN) + " " + newTest.getName());
                            }
                        } else {
                            if (!oldTest.getDate().equals(newTest.getDate()) && settings.isAnnouncedTestDateUpdated()) {
                                sendNotification("Test moved", newTest.getName() + " New Date: " + newTest.getDate().toString(PATTERN) + " Old Date: " + oldTest.getDate().toString(PATTERN));
                            }
                        }
                        if (settings.isNewMark() && ((oldTest != null && oldTest.getMark() != newTest.getMark()) || (oldTest == null && newTest.getMark() != 0.0))) {
                            sendNotification("New Mark", newTest.getName() + " " + newTest.getMark());
                        }
                    }
                }

                //Test Reminder
                LocalDateTime now = LocalDateTime.now();
                LocalTime time = new LocalTime(0, 0, 0);
                for (SchoolTest test : newTests) {
                    LocalDateTime testDate = test.getDate().toLocalDateTime(time);
                    if (now.isBefore(testDate)) {
                        List<Duration> durations = reminderMap.get(test.getId());
                        if (durations == null) {
                            durations = new ArrayList<>();
                        }
                        for (Duration reminderDuration : settings.getTestReminders()) {
                            if (!durations.contains(reminderDuration)) {
                                if (new Period(now, testDate).minus(reminderDuration.toPeriod()).toStandardMinutes().getMinutes() < 0) {
                                    sendNotification("Test Reminder", test.getDate().toString(PATTERN) + " " + test.getName());
                                    durations.add(reminderDuration);
                                    reminderMap.put(test.getId(), durations);
                                }
                            }
                        }
                    }
                }

                persister.persist(TESTS_FILE_NAME, newTests);
                persister.persist(REMINDER_NOTIFICATION_FILE_NAME, reminderMap);

            } catch (Exception e) {
                reschedule = false;
                e.printStackTrace();
            } finally {
                if (reschedule) {
                    NotifierService.scheduleJob(getApplicationContext(), 0);
                } else {
                    sendNotification("Ups", "Something went wrong and for security reasons we stopped checking for new information. Open the App once to start the scheduler again.");
                }
            }
        }).start();
        return true;
    }

    private void sendNotification(String title, String text) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNull(notificationManager);
            builder = new Notification.Builder(NotifierService.this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(NotifierService.this);
        }
        Notification notification = builder.setContentTitle(title).setContentText(text).setSmallIcon(R.drawable.logosephirmobile).setStyle(new Notification.BigTextStyle().bigText(text)).build();
        notificationManager.notify(counter++, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannelIfNull(NotificationManager notificationManager) {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "SephirMobile",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification of new Marks, Announced Tests and more");
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public static void scheduleJob(Context context, int id) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName serviceComponent = new ComponentName(context, NotifierService.class);
        JobInfo.Builder builder = new JobInfo.Builder(id, serviceComponent);
        builder.setMinimumLatency(settings.getLatency());
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        jobScheduler.schedule(builder.build());
    }
}
