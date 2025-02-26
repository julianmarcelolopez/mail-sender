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
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MailSenderApplication.class);
		} catch (ContainerInitializationException e) {
			// Si no se puede inicializar el contenedor, registrar el error y lanzar una excepción en tiempo de ejecución
			e.printStackTrace();
			throw new RuntimeException("No se pudo inicializar la aplicación Spring Boot", e);
		}
	}

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		// Redireccionar la ruta raíz a Swagger UI
		if (input.getPath().equals("/") || input.getPath().equals("/dev")) {
			Map<String, String> headers = new HashMap<>();
			headers.put("Location", "/swagger-ui/index.html");

			return new APIGatewayProxyResponseEvent()
					.withStatusCode(302)
					.withHeaders(headers);
		}

		// Para otras rutas, procesar normalmente a través del contenedor Spring Boot
		AwsProxyRequest awsProxyRequest = new AwsProxyRequest();
		awsProxyRequest.setPath(input.getPath());
		awsProxyRequest.setHttpMethod(input.getHttpMethod());
		awsProxyRequest.setBody(input.getBody());
		awsProxyRequest.setHeaders((SingleValueHeaders) input.getHeaders());
		awsProxyRequest.setQueryStringParameters(input.getQueryStringParameters());
		awsProxyRequest.setPathParameters(input.getPathParameters());

		AwsProxyResponse response = handler.proxy(awsProxyRequest, context);

		return new APIGatewayProxyResponseEvent()
				.withStatusCode(response.getStatusCode())
				.withHeaders(response.getHeaders())
				.withBody(response.getBody())
				.withIsBase64Encoded(response.isBase64Encoded());
	}
}