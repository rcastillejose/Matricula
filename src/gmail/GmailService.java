package gmail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.google.api.services.gmail.model.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface GmailService {
	void setGmailCredentials(GmailCredentials gmailCredentials);

	boolean sendMessage(String recipientAddress, String subject, String body) throws MessagingException, IOException;

	Message sendMessage(String userId, MimeMessage emailContent) throws MessagingException, IOException;

	MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText,
			ArrayList<File> files) throws MessagingException, IOException;

	MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText)
			throws MessagingException, IOException;
}
