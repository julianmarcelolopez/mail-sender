package com.jpdevs.mailsender.service;

public class EmailServiceException extends RuntimeException {
	public EmailServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}