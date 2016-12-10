package badtrack.email;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import badtrack.BadTrack;
import badtrack.BadTrackNotifierInterface;

/**
 * Gmail BadTrack Notifier.
 *
 * @author royer
 */
public class GmailBadTrackNotifier extends GmailClient implements
                                   BadTrackNotifierInterface
{

  /**
   * Instantiates a Gmail BadTrack notifier.
   * 
   * @param pUserName
   * @param pPassword
   */
  public GmailBadTrackNotifier(String pUserName, String pPassword)
  {
    super(pUserName, pPassword);
  }

  @Override
  public void notify(BadTrack pBadTrack, Thread pT, Throwable pE)
  {
    String lErrorMessage = pE.getMessage();
    StackTraceElement[] lStackTrace = pE.getStackTrace();

    String lSubject = "";
    lSubject += pBadTrack.getAppName() + ": ";
    lSubject += lErrorMessage;
    lSubject += " at ";
    if (lStackTrace != null)
      lSubject += lStackTrace[0].toString();

    String lMessage = "";
    lMessage += "Report for app: " + pBadTrack.getAppName() + "\n";
    lMessage += "Date and Time: " + new Date() + "\n";
    lMessage += "Uncaught Exception: \n";
    lMessage += "\n";
    if (pT != null)
      lMessage += "Thread: " + pT.getName() + "\n";
    lMessage += "\n";
    lMessage += "Exception stack trace: \n";
    lMessage += getStackTrace(pE);
    lMessage += "\n";
    lMessage += pBadTrack.getSystemInfo();

    sendMessage(getUserName(), getUserName(), lSubject, lMessage);

  }


  private String getStackTrace(Throwable pE)
  {
    try
    {
      StringWriter lStringWriter = new StringWriter();
      PrintWriter lPrintWriter = new PrintWriter(lStringWriter);
      pE.printStackTrace(lPrintWriter);
      return lStringWriter.toString();
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      return "none";
    }
  }

}
