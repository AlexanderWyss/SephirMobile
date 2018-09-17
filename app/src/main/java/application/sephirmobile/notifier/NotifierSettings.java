package application.sephirmobile.notifier;

import org.joda.time.Duration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class NotifierSettings implements Serializable {
    private final int latency;
    private final boolean sendNewAnnouncedTests;
    private final boolean sendAnnouncedTestUpdates;
    private final boolean sendNewMarks;
    private final boolean sendReminders;
    private final List<Duration> testReminders;
    private final boolean sendExceptions;

    private NotifierSettings(int latency, boolean sendNewAnnouncedTests, boolean sendAnnouncedTestUpdates, boolean sendNewMarks, boolean sendReminders, List<Duration> testReminders, boolean sendExceptions) {
        this.latency = latency;
        this.sendNewAnnouncedTests = sendNewAnnouncedTests;
        this.sendAnnouncedTestUpdates = sendAnnouncedTestUpdates;
        this.sendNewMarks = sendNewMarks;
        this.sendReminders = sendReminders;
        this.testReminders = testReminders;
        this.sendExceptions = sendExceptions;
    }

    public NotifierSettings() {
        this(1800000 /*30 min*/, true, true, true, true, getDefaultTestReminders(), true);
    }

    private static List<Duration> getDefaultTestReminders() {
        List<Duration> defaultTestReminders = new ArrayList<>();
        defaultTestReminders.add(new Duration(21600000)); //6 hours
        defaultTestReminders.add(new Duration(86400000)); //24 hours
        defaultTestReminders.add(new Duration(259200000)); //3 days
        defaultTestReminders.add(new Duration(69120000)); //8 days
        return defaultTestReminders;
    }

    public int getLatency() {
        return latency;
    }

    public boolean sendNewAnnouncedTests() {
        return sendNewAnnouncedTests;
    }

    public boolean sendAnnouncedTestUpdates() {
        return sendAnnouncedTestUpdates;
    }

    public boolean sendNewMarks() {
        return sendNewMarks;
    }

    public boolean sendReminders() {
        return sendReminders;
    }

    public List<Duration> getTestReminders() {
        return testReminders;
    }

    public boolean sendExceptions() {
        return sendExceptions;
    }
}
