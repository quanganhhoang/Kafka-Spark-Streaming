package postlambda;


import lombok.Data;

@Data
public class GatewayRequest {
    PathData pathParameters;
    String body;
}
