package resort;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.GatewayResponse;
import java.util.HashMap;
import java.util.Map;
import model.Resort;
import model.ResortList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.HikariDS;

public class ResortService implements RequestHandler<Object, GatewayResponse> {
  private static final Logger logger = LogManager.getLogger(ResortService.class);
  private static final Gson gson = new Gson();

  public GatewayResponse handleRequest(final Object input, final Context context) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");

    String sqlQuery = "SELECT * FROM Resorts";
    try (Connection conn = HikariDS.getConnection();
        PreparedStatement pst = conn.prepareStatement(sqlQuery);
        ResultSet rs = pst.executeQuery()) {
      List<Resort> resortList = new ArrayList<>();
      while (rs.next()) {
        logger.info(rs.getString("id") + " - " + rs.getString("resort_name"));
        resortList.add(new Resort(rs.getString("id"), rs.getString("resort_name")));
      }

      String body = gson.toJson(new ResortList(resortList));
      return new GatewayResponse(body, headers, 200);
    } catch (SQLException e) {
      logger.error(e);
      return new GatewayResponse("{}", headers, 500);
    }

  }
}
