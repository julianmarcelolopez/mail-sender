package com.jpdevs.mailsender;

import java.util.Map;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private static ConfigurableApplicationContext applicationContext;

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		if (applicationContext == null) {
			applicationContext = SpringApplication.run(MailSenderApplication.class);
		}

		// Redirect root path to Swagger UI
		if (input.getPath().equals("/") || input.getPath().equals("/dev")) {
			return new APIGatewayProxyResponseEvent()
					.withStatusCode(302)
					.withHeaders(Map.of("Location", "/swagger-ui/index.html"));
		}

		// Handle other requests through your existing controllers
		return new APIGatewayProxyResponseEvent()
				.withStatusCode(200);
	}
}