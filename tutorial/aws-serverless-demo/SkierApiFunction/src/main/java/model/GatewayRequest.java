package model;

public class GatewayRequest {
  private String body;
  private SkierPostPathParam pathParameters;

  public GatewayRequest() {}

  public GatewayRequest(String body, SkierPostPathParam pathParameters) {
    this.body = body;
    this.pathParameters = pathParameters;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public SkierPostPathParam getPathParameters() {
    return pathParameters;
  }

  public void setPathParameters(SkierPostPathParam pathParameters) {
    this.pathParameters = pathParameters;
  }
}
