package com.jpdevs.mailsender;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//	private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
//
//	static {
//		try {
//			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MailSenderApplication.class);
//		} catch (ContainerInitializationException e) {
//			throw new RuntimeException("Could not initialize Spring Boot application", e);
//		}
//	}
//
//	@Override
//	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
//		// Redirect root path to Swagger UI
//		if (input.getPath().equals("/") || input.getPath().equals("/dev")) {
//			Map<String, String> headers = new HashMap<>();
//			headers.put("Location", "/swagger-ui.html");
//
//			return new APIGatewayProxyResponseEvent()
//					.withStatusCode(302)
//					.withHeaders(headers);
//		}
//
//		try {
//			// Convert the APIGatewayProxyRequestEvent to a HttpServletRequest
//			// This is a more direct approach that bypasses the need for specific conversions
//
//			// Create an AwsProxyHttpServletRequest manually instead of using the awsProxyRequest.setHeaders
//			ServletContext servletContext = handler.getServletContext();
//
//			// Use reflection to access protected methods if needed
//			Method proxyMethod = SpringBootLambdaContainerHandler.class.getDeclaredMethod(
//					"getContainerResponse",
//					Object.class,
//					Context.class
//			);
//			proxyMethod.setAccessible(true);
//
//			// Use the input directly with the handler
//			Object response = proxyMethod.invoke(handler, input, context);
//
//			if (response instanceof AwsProxyResponse) {
//				AwsProxyResponse awsResponse = (AwsProxyResponse) response;
//				return new APIGatewayProxyResponseEvent()
//						.withStatusCode(awsResponse.getStatusCode())
//						.withHeaders(awsResponse.getHeaders())
//						.withBody(awsResponse.getBody());
//			}
//
//			// Fallback if reflection doesn't work
//			return new APIGatewayProxyResponseEvent()
//					.withStatusCode(500)
//					.withBody("Error processing request");
//		} catch (Exception e) {
//			context.getLogger().log("Error processing request: " + e.getMessage());
//			StringWriter sw = new StringWriter();
//			e.printStackTrace(new PrintWriter(sw));
//			context.getLogger().log(sw.toString());
//
//			return new APIGatewayProxyResponseEvent()
//					.withStatusCode(500)
//					.withBody("Internal server error: " + e.getMessage());
//		}
//	}
//}
public class LambdaHandlerOld implements RequestStreamHandler {
	private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(MailSenderApplication.class);
		} catch (ContainerInitializationException e) {
			throw new RuntimeException("Could not initialize Spring Boot application", e);
		}
	}

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		handler.proxyStream(inputStream, outputStream, context);
	}


}