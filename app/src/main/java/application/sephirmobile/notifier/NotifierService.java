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

import java.util.ArrayList;
import java.util.List;

import application.sephirmobile.R;
import application.sephirmobile.login.LoginUtils;
import application.sephirmobile.sephirinterface.SephirInterface;
import application.sephirmobile.sephirinterface.entitys.AnnouncedTest;
import application.sephirmobile.sephirinterface.entitys.SchoolClass;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;
import application.sephirmobile.sephirinterface.getters.AnnouncedTestGetter;
import application.sephirmobile.sephirinterface.getters.SchoolClassGetter;
import application.sephirmobile.sephirinterface.getters.SchoolTestGetter;

public class NotifierService extends JobService {

    public static final String CHANNEL_ID = "default";
    private static int counter = 0;
    private NotificationManager notificationManager;
    private static int latency = 10000;
    private static final String ANNOUNCED_TESTS_FILE_NAME = "announcedTests.ser";
    private static final String TESTS_FILE_NAME = "tests.ser";

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
                List<AnnouncedTest> newAnnouncedTests = new AnnouncedTestGetter(sephirInterface).get();

                Persister persister = new Persister(NotifierService.this);
                List<AnnouncedTest> oldAnnouncedTests = (List<AnnouncedTest>) persister.load(ANNOUNCED_TESTS_FILE_NAME);
                List<SchoolTest> oldTests = (List<SchoolTest>) persister.load(TESTS_FILE_NAME);

                if (newAnnouncedTests != null) {
                    for (AnnouncedTest announcedTest : newAnnouncedTests) {
                        
                    }
                    if (oldAnnouncedTests != null) {

                    }
                }
                if (oldTests != null && newTests != null) {

                }

                persister.persist(ANNOUNCED_TESTS_FILE_NAME, newAnnouncedTests);
                persister.persist(TESTS_FILE_NAME, newTests);

            } catch (Exception e) {
                reschedule = false;
                e.printStackTrace();
            } finally {
                if (reschedule) {
                    // NotifierService.scheduleJob(getApplicationContext());
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
        Notification notification = builder.setContentTitle(title).setContentText(text).setSmallIcon(R.drawable.logosephirmobile).build();
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

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, NotifierService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(latency);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }
}
