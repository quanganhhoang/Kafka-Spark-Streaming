package sql;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HikariDS {
  private static final Logger logger = LogManager.getLogger(HikariDS.class.getName());

  private static final boolean IS_LOCAL = false;

  private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String LOCALHOST_JDBC_URL = "jdbc:mysql://localhost:3306/skierapi?useSSL=false&serverTimezone=UTC";
  private static final String RDS_USERNAME = IS_LOCAL ? "root" : System.getenv("RDS_USERNAME");
  private static final String RDS_PW = IS_LOCAL ? "root" : System.getenv("RDS_PW");

  private static final String JDBC_URL =
      IS_LOCAL ? LOCALHOST_JDBC_URL
               : System.getenv("RDS_JDBC_URL");

  private static HikariDataSource dataSource = new HikariDataSource();
    // HikariCP uses milliseconds for all time values.
    static {
      logger.info("JDBC URL: " + JDBC_URL);
      logger.info("RDS_USERNAME: " + RDS_USERNAME);
      logger.info("RDS_PW: " + RDS_PW);
      dataSource.setJdbcUrl(JDBC_URL);
      dataSource.setUsername(RDS_USERNAME);
      dataSource.setPassword(RDS_PW);
      dataSource.setMaximumPoolSize(10);
      dataSource.setDriverClassName(JDBC_DRIVER);
      // controls the max time that a connection is allowed to sit idle in the pool
//      dataSource.setIdleTimeout(28740000);
      // controls the min number of idle connections HikariCP tries to maintain in the pool, including both idle and in-use connections
//      dataSource.setMinimumIdle();
      dataSource.setMaxLifetime(120 * 1_000); // controls the maximum lifetime of a connection in the pool
//      dataSource.setLeakDetectionThreshold(60000);
      dataSource.addDataSourceProperty("cachePrepStmts", "true");
      dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
      dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      dataSource.addDataSourceProperty("useServerPrepStmts", "true");
      dataSource.addDataSourceProperty("useLocalSessionState", "true");
      dataSource.addDataSourceProperty("rewriteBatchedStatements", "true");
      dataSource.addDataSourceProperty("cacheResultSetMetadata", "true");
      dataSource.addDataSourceProperty("cacheServerConfiguration", "true");
      dataSource.addDataSourceProperty("elideSetAutoCommits", "true");
      dataSource.addDataSourceProperty("maintainTimeStats", "false");
    }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
