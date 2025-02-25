package com.jpdevs.mailsender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailProperties {
	private String from;

	public EmailProperties() {
	}

	public EmailProperties(String from) {
		this.from = from;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}