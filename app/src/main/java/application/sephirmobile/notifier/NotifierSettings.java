package application.sephirmobile.notifier;

import org.joda.time.Duration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotifierSettings implements Serializable{
    private int latency;
    private boolean newAnnouncedTest;
    private boolean announcedTestDateUpdated;
    private boolean newMark;
    private List<Duration> testReminders;

    public NotifierSettings(int latency, boolean newAnnouncedTest, boolean announcedTestDateUpdated, boolean newMark, List<Duration> testReminders) {
        this.latency = latency;
        this.newAnnouncedTest = newAnnouncedTest;
        this.announcedTestDateUpdated = announcedTestDateUpdated;
        this.newMark = newMark;
        this.testReminders = testReminders;
    }

    public NotifierSettings() {
        this(getDefaultLatency(), getDefaultNewAnnouncedTest(), getDefaultAnnouncedTestDateUpdated(), getDefaultNewMark(), getDefaultTestReminders());
    }

    public static List<Duration> getDefaultTestReminders() {
        List<Duration> defaultTestReminders = new ArrayList<>();
        defaultTestReminders.add(new Duration(21600000)); //6 hours
        defaultTestReminders.add(new Duration(86400000)); //24 hours
        defaultTestReminders.add(new Duration(259200000)); //3 days
        return defaultTestReminders;
    }
    public static int getDefaultLatency() {return 1800000;/*30 mins*/};
    public static boolean getDefaultNewAnnouncedTest() {return true;}
    public static boolean getDefaultAnnouncedTestDateUpdated() {return true;}
    public static boolean getDefaultNewMark() {return true;}


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

    public List<Duration> getTestReminders() {
        return testReminders;
    }
}
