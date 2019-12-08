package handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import kafka.KafkaConfig;
import kafka.StreamProducer;
import model.GatewayRequest;
import model.GatewayResponse;
import java.util.HashMap;
import java.util.Map;
import model.PostBody;
import model.SkierPostPathParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sql.HikariDS;

public class SkierService implements RequestHandler<GatewayRequest, GatewayResponse> {
  private static final Logger logger = LogManager.getLogger(ResortService.class);
  private static final Gson gson = new Gson();

  public GatewayResponse handleRequest(final GatewayRequest input, final Context context) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    headers.put("X-Custom-Header", "application/json");

//    logger.info("path param: " + input.getPathParameters());
//    logger.info("body: " + input.getBody());

    SkierPostPathParam params = input.getPathParameters();

    int resortId = params.getResortId();
    String seasonId = params.getSeasonId();
    int dayId = params.getDayId();
    int skierId = params.getSkierId();

    PostBody body = gson.fromJson(input.getBody(), PostBody.class);
    int time = body.getTime();
    int liftId = body.getLiftId();
    int vertical = liftId * 10;

    // send liftId to Kafka topic for analysis
    StreamProducer.sendKafkaMessage(KafkaConfig.INPUT_TOPIC, Integer.toString(liftId));

    boolean res;
    try {
      res = postVertical(resortId, seasonId, dayId, skierId, time, liftId, vertical);

      if (res) return new GatewayResponse("{}", headers, 201);
      else return new GatewayResponse("{}", headers, 500);
    } catch (SQLException e) {
      logger.info("ERROR: ", e);

      return new GatewayResponse("{}", headers, 500);
    }
  }

  public boolean postVertical(int resortId, String season, int dayId,
      int skierId, int time, int liftId, int vertical) throws SQLException {
    String sqlStmt = "INSERT INTO LiftRides"
        + " (skier_id, resort_id, season, day_id, lift_id, ride_time, vertical)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?);";

    logger.info("Executing: " + sqlStmt);
    try (Connection conn = HikariDS.getConnection();
        PreparedStatement pst = conn.prepareStatement(sqlStmt)) {
      pst.setInt(1, skierId);
      pst.setInt(2, resortId);
      pst.setString(3, season);
      pst.setInt(4, dayId);
      pst.setInt(5, liftId);
      pst.setInt(6, time);
      pst.setInt(7, vertical);

      int rowAffected = pst.executeUpdate();

      return rowAffected == 1;
    }
  }
}
