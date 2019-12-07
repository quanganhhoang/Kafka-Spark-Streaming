package postlambda;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import dao.ConnectionManager;
import dao.PostDAO;
import model.LiftRide;
import org.json.JSONObject;
import secret.DBSecret;
import secret.SecretAPI;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<GatewayRequest, Object> {

    public Object handleRequest(final GatewayRequest input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        //System.out.println(new Gson().toJson(input));
        DBSecret secret = SecretAPI.getSecret();
        //System.out.println(new Gson().toJson(secret));

        ConnectionManager manager = new ConnectionManager(secret.getUsername(), secret.getPassword(), secret.getHost(), secret.getPort());

        BodyData body = new Gson().fromJson(input.body, BodyData.class);

        System.out.println(new Gson().toJson(input));
        context.getLogger().log(new Gson().toJson(input));
        PostDAO dao = new PostDAO(manager);
        try {
            dao.create(new LiftRide(input.pathParameters.resortID, input.pathParameters.seasonID, input.pathParameters.dayID, input.pathParameters.skierID, body.liftID, body.time, body.liftID * 10));
        } catch (SQLException e) {
            return new GatewayResponse(e.getMessage(), headers, 500);
        }

        //JSONSerializer serializer = new JSONSerializer();
        //String output = serializer.serialize(req);

        return new GatewayResponse("posted", headers, 200);
        /*try {
            final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
            //String output = String.format("{ \"message\": \"hello world\", \"location\": \"%s\" }", pageContents);
            return new GatewayResponse(output, headers, 200);
        } catch (IOException e) {
            return new GatewayResponse("{}", headers, 500);
        }*/
    }

    /*private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }*/
}
