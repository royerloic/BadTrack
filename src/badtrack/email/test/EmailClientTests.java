package badtrack.email.test;

import org.junit.Test;

import badtrack.email.GmailClient;

public class EmailClientTests
{

  @Test
  public void test()
  {
    GmailClient lEmailClient = new GmailClient("ClearVolumeErrors@gmail.com", "!!");
    
    lEmailClient.sendMessage("ClearVolumeErrors@gmail.com", "ClearVolumeErrors@gmail.com", "subject", "text");
  }

}
