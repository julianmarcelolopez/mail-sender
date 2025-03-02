package com.jpdevs.mailsender;

import com.amazonaws.serverless.proxy.model.SingleValueHeaders;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.HashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MailSenderApplication.class);
		} catch (ContainerInitializationException e) {
			throw new RuntimeException("Could not initialize Spring Boot application", e);
		}
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		// Redirect root path to Swagger UI
		if (input.getPath().equals("/") || input.getPath().equals("/dev")) {
			Map<String, String> headers = new HashMap<>();
			headers.put("Location", "/swagger-ui.html");

			return new APIGatewayProxyResponseEvent()
					.withStatusCode(302)
					.withHeaders(headers);
		}

		// Use the handler for all other requests
		AwsProxyRequest awsProxyRequest = new AwsProxyRequest();
		awsProxyRequest.setHttpMethod(input.getHttpMethod());
		awsProxyRequest.setBody(input.getBody());
		awsProxyRequest.setHeaders((SingleValueHeaders) input.getHeaders());
		awsProxyRequest.setQueryStringParameters(input.getQueryStringParameters());
		awsProxyRequest.setPathParameters(input.getPathParameters());

		AwsProxyResponse response = handler.proxy(awsProxyRequest, context);

		return new APIGatewayProxyResponseEvent()
				.withStatusCode(response.getStatusCode())
				.withHeaders(response.getHeaders())
				.withBody(response.getBody());
	}
}