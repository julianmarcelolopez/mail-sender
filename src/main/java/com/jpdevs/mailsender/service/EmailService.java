package com.jpdevs.mailsender.service;

import com.jpdevs.mailsender.config.EmailProperties;
import com.jpdevs.mailsender.model.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
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

	public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine, EmailProperties emailProperties) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
		this.emailProperties = emailProperties;
	}

	private static final Map<String, String> IMAGES = Map.of(
			"logo", "static/images/logo.svg",
			"website-icon", "static/images/social/website-icon.png",
			"email-icon", "static/images/social/email-icon.png",
			"instagram-icon", "static/images/social/instagram-icon.png",
			"whatsapp-icon", "static/images/social/whatsapp-icon.png"
	);

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
		Map<String, Object> variables = new HashMap<>();
		variables.put("title", request.getTitle());
		variables.put("message", request.getMessage());
		variables.put("footer", request.getFooter());
		context.setVariables(variables);

		String htmlContent = templateEngine.process("email-template", context);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

		helper.setFrom(emailProperties.getFrom());
		helper.setTo(request.getTo());
		helper.setSubject(request.getSubject());
		helper.setText(htmlContent, true);

		// Agregar todas las imágenes como recursos inline
		try {
			for (Map.Entry<String, String> image : IMAGES.entrySet()) {
				helper.addInline(image.getKey(), new ClassPathResource(image.getValue()));
			}
//			log.debug("All images added successfully to the email");
		} catch (Exception e) {
//			log.error("Error adding inline images to email", e);
			throw new MessagingException("Error adding inline images", e);
		}

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