package ro.activemall.photoxserver.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ro.activemall.photoxserver.events.SendEmailEvent;

@Component
public class SendEmailService implements ApplicationListener<SendEmailEvent> {
	private static Logger log = Logger.getLogger(SendEmailService.class);

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	TemplateEngine templateEngine;

	// @Autowired
	// private Environment env;

	@Value("${mail.from.recipient}")
	String defaultRecipient;

	@Override
	public void onApplicationEvent(SendEmailEvent event) {
		if (event.getUser() != null) {
			log.info("Event " + SendEmailEvent.toString(event.getType())
					+ " for sending email is here : target recipient "
					+ event.getUser().getUsername());
		} else {
			log.info("Probably test event");
		}
		switch (event.getType()) {
		case SendEmailEvent.TEST:
			log.info(event.getExtra().toString());
			break;
		case SendEmailEvent.REGISTER:
			// getExtra() contine PasswordAndTokenPO
			break;
		case SendEmailEvent.CHANGE_PASSWORD:
			// getExtra() contine String cu noua parola
			break;
		case SendEmailEvent.FORGOT_PASSWORD:// nefolosit inca
			// getExtra()
			break;
		case SendEmailEvent.NEWSLETTER:// nefolosit inca
			// getExtra()
			break;
		}
	}

	@Async
	public void sendEmail() {
		log.info("Sending email started");
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException ex) {
			log.error("Exception", ex);
		}
		log.info("Email sent");
	}

	/*
	 * Send HTML mail (simple)
	 */
	public void sendSimpleMail(final String recipientName,
			final String recipientEmail, final Locale locale)
			throws MessagingException {
		System.out.printf("USING LOCALE : %10s - %s, %s \n", locale.toString(),
				locale.getDisplayName(), locale.getDisplayCountry());
		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
				"UTF-8");
		message.setSubject("Example HTML email (simple)");
		message.setFrom(defaultRecipient);
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = templateEngine.process(
				"emails/email-simple", ctx);
		log.info(htmlContent);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		mailSender.send(mimeMessage);

	}

	public byte[] getFileBytes(String filename) throws IOException {
		File file = new File(filename);

		byte[] b = new byte[(int) file.length()];
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(b);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
			e.printStackTrace();
		} catch (IOException e1) {
			System.out.println("Error Reading The File.");
			e1.printStackTrace();
		}
		return b;
	}

	/*
	 * Send HTML mail with attachment.
	 */
	public void sendMailWithAttachment(final String recipientName,
			final String recipientEmail, final String attachmentFileName,
			final String attachmentContentType, final Locale locale)
			throws MessagingException, IOException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
				true /* multipart */, "UTF-8");
		message.setSubject("Example HTML email with attachment");
		message.setFrom(defaultRecipient);
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = templateEngine.process(
				"emails/email-withattachment", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Add the attachment
		final InputStreamSource attachmentSource = new ByteArrayResource(
				getFileBytes(attachmentFileName));
		message.addAttachment(FilenameUtils.getBaseName(attachmentFileName),
				attachmentSource, attachmentContentType);

		// Send mail
		mailSender.send(mimeMessage);

	}

	/*
	 * Send HTML mail with inline image
	 */
	public void sendMailWithInline(final String recipientName,
			final String recipientEmail, final String imageResourceName,
			final String imageContentType, final Locale locale)
			throws MessagingException, IOException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
		ctx.setVariable("imageResourceName", imageResourceName); // so that we
																	// can
																	// reference
																	// it from
																	// HTML

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
				true /* multipart */, "UTF-8");
		message.setSubject("Example HTML email with inline image");
		message.setFrom(defaultRecipient);
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = templateEngine.process(
				"emails/email-inlineimage", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Add the inline image, referenced from the HTML code as
		// "cid:${imageResourceName}"
		final InputStreamSource imageSource = new ByteArrayResource(
				getFileBytes(imageResourceName));
		message.addInline(FilenameUtils.getBaseName(imageResourceName),
				imageSource, imageContentType);

		// Send mail
		mailSender.send(mimeMessage);

	}

}