package com.jpdevs.mailsender.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class EmailRequest {
	private String to;
	private String subject;
	private String message;
	private String title;
	private String footer;

	public EmailRequest() {
	}

	public EmailRequest(String to, String subject, String message, String title, String footer) {
		this.to = to;
		this.subject = subject;
		this.message = message;
		this.title = title;
		this.footer = footer;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}