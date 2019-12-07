package dao;

import model.LiftRide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PostDAO {
    private ConnectionManager manager;

    public PostDAO(ConnectionManager manager) {
        this.manager = manager;
    }


    public LiftRide create(LiftRide ride) throws SQLException {
        String insert = "INSERT INTO LiftRides(ResortID, SeasonID, DayID, SkierID, LiftID, Time, Vertical) VALUES" +
                "(?,?,?,?,?,?,?)";
        Connection conn = manager.getConnection();
        try (PreparedStatement preparedStatement = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt(1, ride.getResortId());
            preparedStatement.setString(2, ride.getSeasonId());
            preparedStatement.setString(3, ride.getDayId());
            preparedStatement.setInt(4, ride.getSkierId());
            preparedStatement.setShort(5, ride.getLiftId());
            preparedStatement.setShort(6, ride.getTime());
            preparedStatement.setInt(7, ride.getVerticalDistance());
            preparedStatement.executeUpdate();
        }
        return ride;
    }
}
