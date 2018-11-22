package application.sephirmobile.notifier;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
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

    public void sendUpdatedAnnouncedTestNotification(SchoolTest test) {
        if (settings.sendAnnouncedTestUpdates()) {
            sendNotification("Prüfung verschoben", getSubjectAndName(test) + " Neues Datum: " + test.getDate().toString(PATTERN));
        }
    }


    public void sendNewAnnouncedTestNotification(SchoolTest test) {
        if (settings.sendNewAnnouncedTests()) {
            sendNotification("Prüfung angekündigt", test.getDate().toString(PATTERN) + getSubjectAndName(test));
        }
    }

    public void sendNewMarkNotification(SchoolTest test, double averageMark) {
        if (settings.sendNewMarks()) {
            sendNotification("Neue Note", test.getMark() + " " + getSubjectAndName(test) + " Ø" + averageMark);
    }
    }

    public void sendReminder(SchoolTest test) {
        sendNotification("Prüfung Erinnerung", test.getDate().toString(PATTERN) + " " + getSubjectAndName(test));
    }

    public void sendLoginError() {
        sendNotification("Ups", "Login nicht erfolgreich. Melde dich in der App neu an.");
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
            channel.setDescription("Erinnerungen an Prüfungen, neue Noten und mehr");
            notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    private String getSubjectAndName(SchoolTest test) {
        return test.getSubject() + " " + test.getName();
    }
}
