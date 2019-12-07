package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    // User to connect to your database instance. By default, this is "root2".
    //use the user i use to sign into mysqp workbench
    private final String user;
    // Password for the user.
    private final String password;
    // URI to your database server. If running on the same machine, then this is "localhost".
    private final String hostName;
    // Port to your database server. By default, this is 3306.
    private final int port;
    // Name of the MySQL schema that contains your tables.
    private final String schema = "Upik";
    // Default timezone for MySQL server.
    private final String timezone = "UTC";

    private static Connection conn = null;

    public ConnectionManager(String user, String password, String hostName, int port) {
        this.user = user;
        this.password = password;
        this.hostName = hostName;
        this.port = port;
    }

    /** Get the connection to the database instance. */
    public Connection getConnection() throws SQLException {
        if (conn == null) {
            try {
                Properties connectionProperties = new Properties();
                connectionProperties.put("user", user);
                connectionProperties.put("password", password);
                connectionProperties.put("serverTimezone", this.timezone);
                // Ensure the JDBC driver is loaded by retrieving the runtime Class descriptor.
                // Otherwise, Tomcat may have issues loading libraries in the proper order.
                // One alternative is calling this in the HttpServlet init() override.
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new SQLException(e);
                }
                conn = DriverManager.getConnection(
                        "jdbc:mysql://" + hostName + ":" + port + "/" + this.schema + "?useSSL=false",
                        connectionProperties);
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }

        return conn;
    }

}
