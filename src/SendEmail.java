//WORKS
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	
	public static boolean sendNewPassReq(String destEmail, String newPass, String thisUser) { // @ Credit Eduonix
		//Declare recipient's & sender's e-mail id.
	      String destmailid = (!(destEmail==null)) ? destEmail : "secto1140219@mail.wcccd.edu" ;
	      String sendrmailid = "janedoe_cis207@gmx.com";	  
	     //Mention user name and password as per your configuration
	      final String uname = "janedoe_cis207@gmx.com";
	      final String pwd = "0*38pruvEth2THiZa@9P";
	      //We are using relay.jangosmtp.net for sending emails
	      String smtphost = "mail.gmx.com";
	      
	     //Set properties and their values
	      Properties propvls = new Properties();
	      propvls.put("mail.smtp.ssl.trust", "mail.gmx.com");
	      propvls.put("mail.smtp.auth", "true");
	      propvls.put("mail.smtp.starttls.enable", "true");
	      propvls.put("mail.smtp.host", smtphost);
	      propvls.put("mail.smtp.port", "587");
	      //Create a Session object & authenticate uid and pwd
	      Session sessionobj = Session.getInstance(propvls,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(uname, pwd);
		   }
	         });

	      try {
		   //Create MimeMessage object & set values
		   Message messageobj = new MimeMessage(sessionobj);
		   messageobj.setFrom(new InternetAddress(sendrmailid));
		   messageobj.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destmailid));
		   messageobj.setSubject(AppSettings.getAppName()+": Password Reset Request");
		   
		   String msgBody = String.format("Username: %s%nNew Password: %s%nKeep your new password safe by keeping it private!%n%nSincerely,%nThe %s Team 👍", thisUser, newPass, AppSettings.getAppName());
		   messageobj.setText(msgBody);
		  //Now send the message
		   Transport.send(messageobj);
		   //System.out.println("Your email sent successfully....");
		   return true;
	      } catch (MessagingException exp) {
	         return false;
	      }
	}
}