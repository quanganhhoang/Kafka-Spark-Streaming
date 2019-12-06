package resort;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import helloworld.GatewayResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResortService implements RequestHandler<Object, GatewayResponse> {
  public Object handleRequest(final Object input, final Context context) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");
    try {
      final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
      String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);
      return new GatewayResponse(output, headers, 200);
    } catch (IOException e) {
      return new GatewayResponse("{}", headers, 500);
    }
  }
}
