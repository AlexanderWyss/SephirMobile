package android.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** This is a Mock class for the slf4j-android Logger to use when tests are run */
public final class Log {
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
  public static final int VERBOSE = 2;
  public static final int DEBUG = 3;
  public static final int INFO = 4;
  public static final int WARN = 5;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;

  public static boolean isLoggable(String tag, int level) {
    return true;
  }

  public static int println(int priority, String tag, String msg) {
    System.out.println(LocalDateTime.now().format(DATE_TIME_FORMATTER) + " [" + getLogLevel(priority) + "] " + tag
        + ": " + filter(msg));
    return 1;
  }

  private static void println(int priority, String tag, String msg, Throwable tr) {
    println(priority, tag, msg);
    System.out.println(getStackTraceString(tr));
  }

  private static String filter(String msg) {
    return msg.replaceAll("passwort=\\[(.*?)\\]", "passwort=[*]");
  }

  private static String getLogLevel(int priority) {
    String level = "";
    switch (priority) {
      case 2:
        level = "VERBOSE";
        break;
      case 3:
        level = "DEBUG";
        break;
      case 4:
        level = "INFO";
        break;
      case 5:
        level = "WARN";
        break;
      case 6:
        level = "ERROR";
        break;
      case 7:
        level = "ASSERT";
        break;
    }
    return level;
  }

  public static String getStackTraceString(Throwable tr) {
    tr.printStackTrace();
    return "";
  }

  public static int d(String tag, String msg, Throwable tr) {
    println(DEBUG, tag, msg, tr);
    return 1;
  }

  public static int d(String tag, String msg) {
    println(DEBUG, tag, msg);
    return 1;
  }

  public static int e(String tag, String msg) {
    println(ERROR, tag, msg);
    return 1;
  }

  public static int e(String tag, String msg, Throwable tr) {
    println(ERROR, tag, msg, tr);
    return 1;
  }

  public static int i(String tag, String msg, Throwable tr) {
    println(INFO, tag, msg, tr);
    return 1;
  }

  public static int i(String tag, String msg) {
    println(INFO, tag, msg);
    return 1;
  }

  public static int v(String tag, String msg) {
    println(VERBOSE, tag, msg);
    return 1;
  }

  public static int v(String tag, String msg, Throwable tr) {
    println(VERBOSE, tag, msg, tr);
    return 1;
  }

  public static int w(String tag, Throwable tr) {
    println(WARN, tag, "", tr);
    return 1;
  }

  public static int w(String tag, String msg, Throwable tr) {
    println(WARN, tag, msg, tr);
    return 1;
  }

  public static int w(String tag, String msg) {
    println(WARN, tag, msg);
    return 1;
  }

  public static int wtf(String tag, String msg) {
    println(ASSERT, tag, msg);
    return 1;
  }

  public static int wtf(String tag, Throwable tr) {
    println(ASSERT, tag, "", tr);
    return 1;
  }

  public static int wtf(String tag, String msg, Throwable tr) {
    println(ASSERT, tag, msg, tr);
    return 1;
  }
}
