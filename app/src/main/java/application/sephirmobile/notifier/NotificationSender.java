package application.sephirmobile.notifier;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.List;
import java.util.Map;

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
        if (settings.isAnnouncedTestDateUpdated()) {
            sendNotification("Test moved", newTest.getName() + " New Date: " + newTest.getDate().toString(PATTERN) + " Old Date: " + oldTest.getDate().toString(PATTERN) + " " + newTest.getSubject());
        }
    }


    public void sendNewAnnouncedTestNotification(SchoolTest newTest) {
        if (settings.isNewAnnouncedTest()) {
            sendNotification("New Test", newTest.getDate().toString(PATTERN) + " " + newTest.getName() + " " + newTest.getSubject());
        }
    }

    public void sendNewMarkNotification(SchoolTest newTest) {
        if (settings.isNewMark()) {
            sendNotification("New Mark", newTest.getName() + " " + newTest.getMark() + " " + newTest.getSubject());
        }
    }

    public void sendReminder(SchoolTest test) {
        sendNotification("Test Reminder", test.getDate().toString(PATTERN) + " " + test.getName() + " " + test.getSubject());
    }

    public void sendLoginError() {
        sendNotification("Ups", "Login not Successful.");
    }

    public void sendExceptionMessage(Exception e) {
        if (settings.sendExceptions()) {
            sendNotification("Exception", "An exception occoured while checking for new Tests: " + e.getLocalizedMessage());
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
