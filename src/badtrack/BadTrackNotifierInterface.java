package badtrack;

/**
 * BadTrack notifier interface.
 *
 * @author royer
 */
public interface BadTrackNotifierInterface
{

  /**
   * This method is called when requesting that the notifir send an exception
   * report.
   * 
   * @param pBadTrack
   *          BadTrack object
   * @param pT
   *          thread
   * @param pE
   *          exception
   */
  void notify(BadTrack pBadTrack, Thread pT, Throwable pE);

}
