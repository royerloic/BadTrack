package badtrack.email;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
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
    lMessage += "Filters: "+pBadTrack.getFilters()+ "\n";
    lMessage += "Unique ID: "+getUniqueId()+ "\n";
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
    
    
    if(pBadTrack.passesFilters(lSubject+lMessage))
      sendMessage(getUserName(), getUserName(), lSubject, lMessage);

  }

  private String getUniqueId()
  {
    try
    {
      byte[] lId = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
      int lHashCode = Math.abs(Arrays.hashCode(lId));
      return ""+lHashCode;
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      return ""; 
    }
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
