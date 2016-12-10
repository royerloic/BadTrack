package badtrack;

import java.lang.Thread.UncaughtExceptionHandler;

import badtrack.optin.BadTrackOptInDialog;

/**
 * BadTrack -- Uses email to send bug reports (uncaught exceptions) to a
 * notification service (Gmail fro example)
 *
 * @author royer
 */
public class BadTrack
{

  private final BadTrackNotifierInterface mBackTrackNotifier;
  private final String mAppName;
  private volatile boolean mNotificationAllowed = false;

  private volatile UncaughtExceptionHandler mExistingDefaultUncaughtExceptionHandler =
                                                                                     null;
  private String mSystemInfo = "";

  /**
   * Instantiates a BadTrack object for a given app and notifier.
   * 
   * @param pAppName
   * @param pBackTrackNotifier
   */
  public BadTrack(String pAppName,
                  BadTrackNotifierInterface pBackTrackNotifier)
  {
    super();
    mAppName = pAppName;
    mBackTrackNotifier = pBackTrackNotifier;
  }

  /**
   * Asks with a Swing dialog if it is ok to send bug reports.
   * 
   * @param pTitle
   *          dialog title
   * @param pMessage
   *          message
   * @return true if bug reports are allowed.
   */
  public boolean askWithSwing(String pTitle, String pMessage)
  {
    mNotificationAllowed =
                         BadTrackOptInDialog.askWithSwing(getAppName(),
                                                    pTitle,
                                                    pMessage);

    return mNotificationAllowed;
  }

  /**
   * Appends a string to the 'system info string' that describes additional
   * important information about the system.
   * 
   * @param pInfo
   */
  public void appendSystemInfo(String pInfo)
  {
    mSystemInfo += "\n";
    mSystemInfo += pInfo;
    mSystemInfo += "\n";
  }

  /**
   * Appends basic system info (os, cpu, memory...)
   */
  public void appendBasicSystemInfo()
  {
    mSystemInfo += "\n";
    mSystemInfo += "Basic system info:\n";

    try
    {
      mSystemInfo +=
                  "os.name= " + System.getProperty("os.name") + "\n";
      mSystemInfo += "os.version= " + System.getProperty("os.version")
                     + "\n";
      mSystemInfo +=
                  "os.arch= " + System.getProperty("os.arch") + "\n";
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }

    try
    {
      mSystemInfo += "PROCESSOR_IDENTIFIER= "
                     + System.getenv("PROCESSOR_IDENTIFIER") + "\n";
      mSystemInfo += "PROCESSOR_ARCHITECTURE= "
                     + System.getenv("PROCESSOR_ARCHITECTURE") + "\n";
      mSystemInfo += "PROCESSOR_ARCHITEW6432= "
                     + System.getenv("PROCESSOR_ARCHITEW6432") + "\n";
      mSystemInfo += "NUMBER_OF_PROCESSORS= "
                     + System.getenv("NUMBER_OF_PROCESSORS") + "\n";
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }

    try
    {
      /* Total number of processors or cores available to the JVM */
      mSystemInfo += "Available processors (cores): "
                     + Runtime.getRuntime().availableProcessors()
                     + "\n";

      /* Total amount of free memory available to the JVM */
      mSystemInfo += "Free memory (bytes): "
                     + Runtime.getRuntime().freeMemory() + "\n";

      /* This will return Long.MAX_VALUE if there is no preset limit */
      long maxMemory = Runtime.getRuntime().maxMemory();
      /* Maximum amount of memory the JVM will attempt to use */
      mSystemInfo += "Maximum memory (bytes): "
                     + (maxMemory == Long.MAX_VALUE ? "no limit"
                                                    : maxMemory)
                     + "\n";

      /* Total memory currently available to the JVM */
      mSystemInfo += "Total memory available to JVM (bytes): "
                     + Runtime.getRuntime().totalMemory() + "\n";
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }

    mSystemInfo += "\n";
  }

  /**
   * Starts tracking uncaught exceptions and sending reports if allowed.
   */
  public void startTracking()
  {
    if (mNotificationAllowed)
      try
      {
        mExistingDefaultUncaughtExceptionHandler =
                                                 Thread.getDefaultUncaughtExceptionHandler();

        BadTrack lThis = this;

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
          public void uncaughtException(Thread t, Throwable e)
          {

            if (mBackTrackNotifier != null)
              mBackTrackNotifier.notify(lThis, t, e);

            if (mExistingDefaultUncaughtExceptionHandler != null)
              mExistingDefaultUncaughtExceptionHandler.uncaughtException(t,
                                                                         e);

            e.printStackTrace();
          }
        });
      }
      catch (Throwable e)
      {
        e.printStackTrace();
      }
  }

  /**
   * Stops tracking uncaught exceptions and sending reports.
   */
  public void stopTracking()
  {
    try
    {
      Thread.setDefaultUncaughtExceptionHandler(mExistingDefaultUncaughtExceptionHandler);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Returns the current system info string.
   * 
   * @return
   */
  public String getSystemInfo()
  {
    return mSystemInfo;
  }

  /**
   * Sets the system info string.
   * 
   * @param pSystemInfo
   *          new system info string
   */
  public void setSystemInfo(String pSystemInfo)
  {
    mSystemInfo = pSystemInfo;
  }

  /**
   * Returns the application/plugin/... name.
   * 
   * @return name
   */
  public String getAppName()
  {
    return mAppName;
  }
}