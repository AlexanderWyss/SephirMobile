package application.sephirmobile.notifier;

import org.joda.time.Duration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class NotifierSettings implements Serializable {
    private final int latency;
    private final boolean newAnnouncedTest;
    private final boolean announcedTestDateUpdated;
    private final boolean newMark;
    private final boolean reminders;
    private final List<Duration> testReminders;
    private final boolean exceptions;

    private NotifierSettings(int latency, boolean newAnnouncedTest, boolean announcedTestDateUpdated, boolean newMark, boolean reminders, List<Duration> testReminders, boolean exceptions) {
        this.latency = latency;
        this.newAnnouncedTest = newAnnouncedTest;
        this.announcedTestDateUpdated = announcedTestDateUpdated;
        this.newMark = newMark;
        this.reminders = reminders;
        this.testReminders = testReminders;
        this.exceptions = exceptions;
    }

    public NotifierSettings() {
        this(getDefaultLatency(), getDefaultNewAnnouncedTest(), getDefaultAnnouncedTestDateUpdated(), getDefaultNewMark(), getDefaultReminders(), getDefaultTestReminders(), getDefaultExceptions());
    }

    private static List<Duration> getDefaultTestReminders() {
        List<Duration> defaultTestReminders = new ArrayList<>();
        defaultTestReminders.add(new Duration(21600000)); //6 hours
        defaultTestReminders.add(new Duration(86400000)); //24 hours
        defaultTestReminders.add(new Duration(259200000)); //3 days
        defaultTestReminders.add(new Duration(69120000)); //8 days
        return defaultTestReminders;
    }

    private static int getDefaultLatency() {
        return 1800000;/*30 mins*/
    }

    private static boolean getDefaultNewAnnouncedTest() {
        return true;
    }

    private static boolean getDefaultAnnouncedTestDateUpdated() {
        return true;
    }

    private static boolean getDefaultNewMark() {
        return true;
    }

    private static boolean getDefaultReminders() {
        return true;
    }

    private static boolean getDefaultExceptions() {
        return true;
    }

    public int getLatency() {
        return latency;
    }

    public boolean isNewAnnouncedTest() {
        return newAnnouncedTest;
    }

    public boolean isAnnouncedTestDateUpdated() {
        return announcedTestDateUpdated;
    }

    public boolean isNewMark() {
        return newMark;
    }

    public boolean sendReminders() {
        return reminders;
    }

    public List<Duration> getTestReminders() {
        return testReminders;
    }

    public boolean sendExceptions() {
        return exceptions;
    }
}
