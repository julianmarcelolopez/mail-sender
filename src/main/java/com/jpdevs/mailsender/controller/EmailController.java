package com.jpdevs.mailsender.controller;

import com.jpdevs.mailsender.controller.api.EmailApi;
import lombok.extern.slf4j.Slf4j;
import com.jpdevs.mailsender.model.EmailRequest;
import com.jpdevs.mailsender.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/email")
@Slf4j
public class EmailController implements EmailApi {
	private final EmailService emailService;

	@Autowired
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/html")
	public ResponseEntity<?> sendHtmlEmail(@RequestBody EmailRequest emailRequest) {
		try {
			emailService.sendHtmlEmail(emailRequest);
			return ResponseEntity.ok(new ApiResponse("Email enviado exitosamente", true));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Error al enviar el email: " + e.getMessage(), false));
		}
	}

	@PostMapping("/simple")
	public ResponseEntity<?> sendSimpleEmail(@RequestBody EmailRequest emailRequest) {
		try {
			emailService.sendSimpleMessage(emailRequest);
			return ResponseEntity.ok(new ApiResponse("Email simple enviado exitosamente", true));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse("Error al enviar el email simple: " + e.getMessage(), false));
		}
	}
}