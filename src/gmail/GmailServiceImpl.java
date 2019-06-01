package gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import configuracion.ConfiguracionSegura;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;


public final class GmailServiceImpl implements GmailService {
	
	private static final String APPLICATION_NAME = (new ConfiguracionSegura()).getApplicationName();

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private final HttpTransport httpTransport;
	private GmailCredentials gmailCredentials;

	public GmailServiceImpl(HttpTransport httpTransport) {
		this.httpTransport = httpTransport;
	}

	@Override
	public void setGmailCredentials(GmailCredentials gmailCredentials) {
		this.gmailCredentials = gmailCredentials;
	}

	@Override
	public boolean sendMessage(String recipientAddress, String subject, String body)
			throws MessagingException, IOException {
		Message message = createMessageWithEmail(
				createEmail(recipientAddress, gmailCredentials.getUserEmail(), subject, body));

		return createGmail().users().messages().send(gmailCredentials.getUserEmail(), message).execute().getLabelIds()
				.contains("SENT");
	}

	public Message sendMessage(String userId, MimeMessage emailContent) throws MessagingException, IOException {

		Message message = createMessageWithEmail(emailContent);
		message = createGmail().users().messages().send(gmailCredentials.getUserEmail(), message).execute();
		
		return message;
	}

	private Gmail createGmail() {
		Credential credential = authorize();
		return new Gmail.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException, UnsupportedEncodingException {
		MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
		email.setFrom(new InternetAddress(from,(new ConfiguracionSegura()).getNameFrom()));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);

		return new Message().setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
	}
	
	public MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText,
			ArrayList<File> files) throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from,(new ConfiguracionSegura()).getNameFrom()));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(bodyText, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		if (files != null) {
			for (File file : files) {
				mimeBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);

				mimeBodyPart.setDataHandler(new DataHandler(source));
				mimeBodyPart.setFileName(file.getName());

				multipart.addBodyPart(mimeBodyPart);

			}
		}
		email.setContent(multipart);

		return email;
	}
	public MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText) throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from,(new ConfiguracionSegura()).getNameFrom()));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
		email.setSubject(subject);

		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(bodyText, "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(mimeBodyPart);
		
		email.setContent(multipart);

		return email;
	}

	private Credential authorize() {
		return new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(JSON_FACTORY)
				.setClientSecrets(gmailCredentials.getClientId(), gmailCredentials.getClientSecret()).build()
				.setAccessToken(gmailCredentials.getAccessToken()).setRefreshToken(gmailCredentials.getRefreshToken());
	}

}
