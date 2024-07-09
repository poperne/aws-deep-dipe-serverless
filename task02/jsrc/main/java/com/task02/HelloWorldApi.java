import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldApi implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent request, Context context) {
		System.out.println("Incoming request: " + request);

		if ("/hello".equals(request.getRequestContext().getHttp().getPath())) {
			return generateResponse("Hello from Lambda", 200);
		} else {
			String message = String.format("Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
					request.getRequestContext().getHttp().getPath(), request.getRequestContext().getHttp().getMethod());
			return generateErrorResponse(message, 400);
		}
	}

	private APIGatewayV2HTTPResponse generateResponse(String body, int statusCode) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		return APIGatewayV2HTTPResponse.builder()
				.withHeaders(headers)
				.withStatusCode(statusCode)
				.withBody(body)
				.build();
	}

	private APIGatewayV2HTTPResponse generateErrorResponse(String message, int statusCode) {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");

		Map<String, Object> errorBody = new HashMap<>();
		errorBody.put("statusCode", statusCode);
		errorBody.put("message", message);

		return APIGatewayV2HTTPResponse.builder()
				.withHeaders(headers)
				.withStatusCode(statusCode)
				.withBody(errorBody.toString())
				.build();
	}
}