package com.jpdevs.mailsender.service;

import com.jpdevs.mailsender.config.EmailProperties;
import com.jpdevs.mailsender.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@Slf4j
public class EmailService {
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	private final EmailProperties emailProperties;

	@Autowired
	public EmailService(
			JavaMailSender mailSender,
			TemplateEngine templateEngine,
			EmailProperties emailProperties) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
		this.emailProperties = emailProperties;
	}

	public void sendHtmlEmail(EmailRequest request) {
		try {
			MimeMessage mimeMessage = createHtmlEmail(request);
			mailSender.send(mimeMessage);
//			log.info("HTML email sent successfully to: {}", request.getTo());
		} catch (MessagingException e) {
//			log.error("Failed to send HTML email to: {}", request.getTo(), e);
			throw new EmailServiceException("Error sending HTML email", e);
		}
	}

	public void sendSimpleMessage(EmailRequest request) {
		try {
			SimpleMailMessage message = createSimpleEmail(request);
			mailSender.send(message);
//			log.info("Simple email sent successfully to: {}", request.getTo());
		} catch (Exception e) {
//			log.error("Failed to send simple email to: {}", request.getTo(), e);
			throw new EmailServiceException("Error sending simple email", e);
		}
	}

	private MimeMessage createHtmlEmail(EmailRequest request) throws MessagingException {
		Context context = new Context();
		context.setVariables(Map.of(
				"title", request.getTitle(),
				"message", request.getMessage(),
				"footer", request.getFooter()
		));

		String htmlContent = templateEngine.process("email-template", context);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

		helper.setFrom(emailProperties.getFrom());
		helper.setTo(request.getTo());
		helper.setSubject(request.getSubject());
		helper.setText(htmlContent, true);

		return mimeMessage;
	}

	private SimpleMailMessage createSimpleEmail(EmailRequest request) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailProperties.getFrom());
		message.setTo(request.getTo());
		message.setSubject(request.getSubject());
		message.setText(request.getMessage());
		return message;
	}
}