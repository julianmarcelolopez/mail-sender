package com.jpdevs.mailsender.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI mailSenderOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Mail Sender API")
						.description("API para el envío de emails")
						.version("1.0")
						.contact(new Contact()
								.name("JP Devs")
								.email("contact@jpdevs.com")));
	}
}