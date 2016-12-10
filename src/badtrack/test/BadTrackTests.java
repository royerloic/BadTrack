package badtrack.test;

import static org.junit.Assert.*;

import org.junit.Test;

import badtrack.BadTrack;
import badtrack.email.GmailBadTrackNotifier;

public class BadTrackTests
{

  @Test
  public void testBasic() throws InterruptedException
  {
    try
    {

      BadTrack lBadTrack =
                         new BadTrack("test App",
                                      (b,
                                       t,
                                       e) -> System.out.println("Caught: thread="
                                                                + t
                                                                + " exception="
                                                                + e));
      lBadTrack.appendBasicSystemInfo();

      lBadTrack.startTracking();

      Runnable lRunnable = () -> {
        throw new RuntimeException("Boom!");
      };

      Thread lThread = new Thread(lRunnable);
      lThread.start();
      Thread.sleep(100);

      lBadTrack.stopTracking();

      lThread = new Thread(lRunnable);
      lThread.start();
      Thread.sleep(100);

    }
    catch (Throwable e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void testEmail() throws InterruptedException
  {
    try
    {

      GmailBadTrackNotifier lEmailNotifier =
                                   new GmailBadTrackNotifier("ClearVolumeErrors@gmail.com",
                                                     "!!");

      BadTrack lBadTrack = new BadTrack("Test App", lEmailNotifier);

      lBadTrack.askWithSwing("Help Debug ~~APP~~",
                             "Lorem ipsum dolor sit amet, consectetur adipiscing elit. \n"
                             + "Vivamus convallis, odio sit amet pulvinar facilisis, \n"
                             + "felis felis rhoncus dolor, non ullamcorper neque magna a ex.");

      lBadTrack.appendBasicSystemInfo();

      lBadTrack.startTracking();

      Runnable lRunnable = () -> {
        throw new RuntimeException("Boom!");
      };
      Thread lThread = new Thread(lRunnable);
      lThread.start();
      Thread.sleep(1000);

      lBadTrack.stopTracking();

      lThread = new Thread(lRunnable);
      lThread.start();
      Thread.sleep(1000);
      
      
      Thread.sleep(3000);
    }
    catch (Throwable e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
