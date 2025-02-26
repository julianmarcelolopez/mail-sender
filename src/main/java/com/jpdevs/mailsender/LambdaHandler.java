package com.jpdevs.mailsender;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.SingleValueHeaders;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.Map;

public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			// Inicializa el handler correctamente para aplicaciones servlet
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MailSenderApplication.class);
		} catch (ContainerInitializationException e) {
			e.printStackTrace();
			throw new RuntimeException("No se pudo inicializar la aplicación Spring Boot", e);
		}
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		// Redirige la raíz a Swagger UI
		if ("/".equals(input.getPath()) || "/dev".equals(input.getPath())) {
			Map<String, String> headers = new HashMap<>();
			headers.put("Location", "/swagger-ui/index.html");

			return new APIGatewayProxyResponseEvent()
					.withStatusCode(302)
					.withHeaders(headers);
		}

		// Convierte la solicitud API Gateway en AwsProxyRequest
		AwsProxyRequest awsProxyRequest = new AwsProxyRequest();
		awsProxyRequest.setPath(input.getPath());
		awsProxyRequest.setHttpMethod(input.getHttpMethod());
		awsProxyRequest.setBody(input.getBody());
		SingleValueHeaders singleValueHeaders = new SingleValueHeaders();
		if (input.getHeaders() != null) {
			input.getHeaders().forEach(singleValueHeaders::put);
		}
		awsProxyRequest.setHeaders(singleValueHeaders);
		awsProxyRequest.setQueryStringParameters(input.getQueryStringParameters());
		awsProxyRequest.setPathParameters(input.getPathParameters());

		// Procesa la solicitud con Spring Boot
		AwsProxyResponse response = handler.proxy(awsProxyRequest, context);

		return new APIGatewayProxyResponseEvent()
				.withStatusCode(response.getStatusCode())
				.withHeaders(response.getHeaders())
				.withBody(response.getBody())
				.withIsBase64Encoded(response.isBase64Encoded());
	}
}