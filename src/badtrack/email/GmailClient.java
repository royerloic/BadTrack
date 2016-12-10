package badtrack.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Basic Gmail email client.
 *
 * @author royer
 */
public class GmailClient
{

  private String mUserName;
  private String mPassword;
  private Properties mProperties;

  /**
   * Constructs an email client for a given Gmail username and password
   * 
   * @param pUserName
   * @param pPassword
   */
  public GmailClient(String pUserName, String pPassword)
  {
    super();
    setUserName(pUserName);
    setPassword(pPassword);

    mProperties = new Properties();
    mProperties.put("mail.smtp.auth", "true");
    mProperties.put("mail.smtp.starttls.enable", "true");
    mProperties.put("mail.smtp.host", "smtp.gmail.com");
    mProperties.put("mail.smtp.port", "587");
  }

  /**
   * Sends a message.
   * 
   * @param pFromEmail
   *          source email
   * @param pToEmail
   *          destination email
   * @param pSubject
   *          subkect
   * @param pText
   *          text
   */
  public void sendMessage(String pFromEmail,
                          String pToEmail,
                          String pSubject,
                          String pText)
  {
    Session lSession =
                     Session.getInstance(mProperties,
                                         new javax.mail.Authenticator()
                                         {
                                           protected PasswordAuthentication getPasswordAuthentication()
                                           {
                                             return new PasswordAuthentication(getUserName(),
                                                                               getPassword());
                                           }
                                         });

    try
    {
      Message message = new MimeMessage(lSession);
      message.setFrom(new InternetAddress(pFromEmail));
      message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(pToEmail));
      message.setSubject(pSubject);
      // message.setContent(pText, "text/html");
      message.setText(pText);

      System.out.println("send email: " + pSubject + "\n" + pText);
      Transport.send(message);
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Returns user name
   * 
   * @return user name
   */
  public String getUserName()
  {
    return mUserName;
  }

  /**
   * Sets user name
   * 
   * @param pUserName
   *          user name
   */
  public void setUserName(String pUserName)
  {
    mUserName = pUserName;
  }

  /**
   * Returns password
   * 
   * @return password
   */
  public String getPassword()
  {
    return mPassword;
  }

  /**
   * Sets password
   * 
   * @param pPassword
   *          password
   */
  public void setPassword(String pPassword)
  {
    mPassword = pPassword;
  }

}
