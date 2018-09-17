package application.sephirmobile.notifier;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import application.sephirmobile.R;
import application.sephirmobile.sephirinterface.entitys.SchoolTest;

public class NotificationSender {

    private final NotificationManager notificationManager;
    private NotifierSettings settings;
    private final Context context;
    private static int counter = 0;
    private static final String CHANNEL_ID = "default";
    private static final String PATTERN = "dd.MM.yyyy";

    public NotificationSender(Context context, NotifierSettings settings) {
        this.context = context;
        this.settings = settings;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendUpdatedAnnouncedTestNotification(SchoolTest newTest, SchoolTest oldTest) {
        if (settings.sendAnnouncedTestUpdates()) {
            sendNotification("Test moved", newTest.getName() + " New Date: " + newTest.getDate().toString(PATTERN) + " Old Date: " + oldTest.getDate().toString(PATTERN) + " " + newTest.getSubject());
        }
    }


    public void sendNewAnnouncedTestNotification(SchoolTest newTest) {
        if (settings.sendNewAnnouncedTests()) {
            sendNotification("New Test", newTest.getDate().toString(PATTERN) + " " + newTest.getName() + " " + newTest.getSubject());
        }
    }

    public void sendNewMarkNotification(SchoolTest newTest, double averageMark) {
        if (settings.sendNewMarks()) {
            sendNotification("New Mark", newTest.getMark() + " " + newTest.getName() + " " + newTest.getSubject() + " Ø" + averageMark);
        }
    }

    public void sendReminder(SchoolTest test) {
        sendNotification("Test Reminder", test.getDate().toString(PATTERN) + " " + test.getName() + " " + test.getSubject());
    }

    public void sendLoginError() {
        sendNotification("Ups", "Login not Successful. Restart the app completely to start checking for updates again.");
    }

    public void sendExceptionMessage(Exception e) {
        if (settings.sendExceptions()) {
            sendNotification("Exception", "An exception occurred while checking for new Tests: " + e.getLocalizedMessage());
        }
    }

    private void sendNotification(String title, String text) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelIfNull(notificationManager);
            builder = new Notification.Builder(context, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(context);
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

}
