package fi.iki.veekoo.arkisto;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	private String from;
	private String to;
	private String bcc;
	private String subject;
	private String text;
	private String date;
	private String path;

	public SendMail(String from, String to, String bcc, String subject, String text, String date, String path) {
		this.from = from;
		this.to = to;
		this.bcc = bcc;
		this.subject = subject;
		this.text = text;
		this.date = date;
		this.path = path;

	}

	public synchronized void send() throws MessagingException {

		try {
			Properties props = new Properties();
			props.put("mail.smtp.localhost", NavigatorUI.getConfig("smtpHost"));
			props.put("mail.smtp.host", NavigatorUI.getConfig("smtpHost"));
			props.put("mail.smtp.port", NavigatorUI.getConfig("port"));

			Session mailSession = Session.getDefaultInstance(props);
			Message simpleMessage = new MimeMessage(mailSession);

			InternetAddress fromAddress = null;
			InternetAddress toAddress = null;
			InternetAddress bccAddress = null;
			try {
				fromAddress = new InternetAddress(from);
				toAddress = new InternetAddress(to);
			} catch (Exception e) {
				System.out.println("SendMail.AddressException");
				e.printStackTrace();
			}

			if (path == null) {
				try {
					simpleMessage.setFrom(fromAddress);
					simpleMessage.setRecipient(RecipientType.TO, toAddress);
					simpleMessage.setSubject(subject);
					simpleMessage.setText(text);
				} catch (MessagingException e1) {
					System.out.println("SendMail.simpleMessage");
					e1.printStackTrace();
				}
				try {
					Transport.send(simpleMessage);
					//System.out.println("SendMail.Transport.send ");
				} catch (Exception e) {
					System.out.println("SendMail.Transport.send error");
					e.printStackTrace();
				}
				if (bcc != null) {
					try {
						bccAddress = new InternetAddress(bcc);
					} catch (Exception e) {
						System.out.println("SendMail.AddressException");
						e.printStackTrace();
					}
					try {
						simpleMessage.setRecipient(RecipientType.TO, bccAddress);
						Transport.send(simpleMessage);
						System.out.println("SendMail.Transport.send");
					} catch (Exception e) {
						System.out.println("SendMail.MessagingException e");
						e.printStackTrace();
					}
				}
			} else {
				// send attachment

				// Create a default MimeMessage object.
				MimeMessage message = new MimeMessage(mailSession);

				try {
					message.setFrom(fromAddress);
					message.setRecipient(RecipientType.TO, toAddress);
					message.setSubject(subject + " " + date);

				} catch (MessagingException e2) {
					System.out.println("MessagingException e2");
					e2.printStackTrace();
				}

				// Create the message part
				BodyPart messageBodyPart = new MimeBodyPart();

				// Fill the message
				messageBodyPart.setText(text);

				// Create a multipart message
				Multipart multipart = new MimeMultipart();

				// Set text message part
				try {
					multipart.addBodyPart(messageBodyPart);
				} catch (MessagingException e1) {
					System.out.println("MessagingException e1");
					e1.printStackTrace();
				}

				// Part two is attachment
				messageBodyPart = new MimeBodyPart();

				DataSource source = new FileDataSource(path);

				try {
					messageBodyPart.setDataHandler(new DataHandler(source));
				} catch (MessagingException e1) {
					System.out.println("MessagingException e1");
					e1.printStackTrace();
				}
				try {
					messageBodyPart.setFileName(new File(path).getName());
				} catch (MessagingException e1) {
					System.out.println("MessagingException e1");
					e1.printStackTrace();
				}

				try {
					multipart.addBodyPart(messageBodyPart);
				} catch (MessagingException e1) {
					System.out.println("MessagingException e1");
					e1.printStackTrace();
				}

				// Send the complete message parts
				try {
					message.setContent(multipart);
				} catch (MessagingException e1) {
					System.out.println("MessagingException e1");
					e1.printStackTrace();
				}

				// Send message
				try {
					Transport.send(message);
				} catch (MessagingException e1) {
					System.out.println("MessagingException e1");
					e1.printStackTrace();
				}

				if (bcc != null) {
					try {
						bccAddress = new InternetAddress(bcc);
					} catch (Exception e) {
						System.out.println("SendMail.AddressException");
						e.printStackTrace();
					}
					try {
						message.setRecipient(RecipientType.TO, bccAddress);
						Transport.send(message);
						System.out.println("NewAccountWindow.send 2");
					} catch (Exception e) {
						System.out.println("SendMail.MessagingException");
						e.printStackTrace();
					}
				}

			}

		} catch (Exception e) {
			System.out.println("Exception e");
			e.printStackTrace();
		}
	}
}
