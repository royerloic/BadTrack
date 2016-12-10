package badtrack.optin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class BadTrackOptInDialog
{

  /**
   * Asks to opt-in using a Swing dialog.
   * 
   * @param pAppName
   *          aplication name
   * @param pTitle
   *          dialog window title
   * @param pMessage
   *          text in dialog
   * @return true if allowed, false otherwise
   */
  public static boolean askWithSwing(String pAppName,
                                     String pTitle,
                                     String pMessage)
  {

    Callable<Boolean> lAsk = () -> {
      Object[] options =
      { "Yes, please", "No, thanks" };
      int n = JOptionPane.showOptionDialog(null,
                                           pMessage,
                                           pTitle,
                                           JOptionPane.YES_NO_OPTION,
                                           JOptionPane.QUESTION_MESSAGE,
                                           UIManager.getIcon("OptionPane.questionIcon"),
                                           options,
                                           options[0]);

      if (n == JOptionPane.CLOSED_OPTION)
        return true;
      else if (n == JOptionPane.YES_OPTION)
        return true;
      else
        return false;
    };

    return cacheAnswer(pAppName, lAsk);

  }

  /**
   * Caches the answer on the disk.
   * 
   * @param pAppName
   *          application name
   * @param pAskCallable
   *          code to execute to ask for permission.
   * @return
   */
  private static boolean cacheAnswer(String pAppName,
                                     Callable<Boolean> pAskCallable)
  {
    try
    {
      File lBadTrackFolder = new File(System.getProperty("user.home"),
                                      ".badtrack");
      lBadTrackFolder.mkdirs();

      File lAnswerCacheFile =
                            new File(lBadTrackFolder,
                                     pAppName.toLowerCase()
                                             .trim() + ".answer.txt");

      if (lAnswerCacheFile.exists())
      {
        Scanner lScanner = new Scanner(lAnswerCacheFile);
        String lCachedAnswerString = lScanner.nextLine()
                                             .trim()
                                             .toLowerCase();
        lScanner.close();

        if (lCachedAnswerString.equals("yes"))
          return true;
        else if (lCachedAnswerString.equals("no"))
          return false;
      }
      else
      {
        boolean lAnswer = pAskCallable.call();

        FileWriter lFileWriter = new FileWriter(lAnswerCacheFile);
        lFileWriter.append(lAnswer ? "yes" : "no");
        lFileWriter.close();

        return lAnswer;
      }
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      return false;
    }

    return false;
  }

}
