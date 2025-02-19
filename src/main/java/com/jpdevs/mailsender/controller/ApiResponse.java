package com.jpdevs.mailsender.controller;

public class ApiResponse {
	private String message;
	private boolean success;


	public ApiResponse(String message, boolean success) {
		this.message = message;
		this.success = success;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public boolean isSuccess() {
		return success;
	}
}
