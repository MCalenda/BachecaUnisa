package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DriverManagerConnectionPool {

  private static List<Connection> freeDbConnections;

  static {
    freeDbConnections = new LinkedList<Connection>();
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static synchronized Connection createDbConnection() throws SQLException {
    String host     = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    String username = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    String password = System.getenv("DB_PASS") != null ? System.getenv("DB_PASS") : "0000";
    String dbName   = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "Bacheca";

    Connection newConnection = DriverManager.getConnection(
        "jdbc:mysql://" + host + ":3306/" + dbName
        + "?zeroDateTimeBehavior=convertToNull&useSSL=false", username, password);
    newConnection.setAutoCommit(false);
    return newConnection;
  }

  public static synchronized Connection getConnection() throws SQLException {
    Connection connection;
    if (!freeDbConnections.isEmpty()) {
      connection = (Connection) freeDbConnections.get(0);
      freeDbConnections.remove(0);
      try {
        if (connection.isClosed()) {
          connection = getConnection();
        }
      } catch (SQLException e) {
        connection.close();
        connection = getConnection();
      }
    } else {
      connection = createDbConnection();
    }
    return connection;
  }

  public static synchronized void releaseConnection(Connection connection) throws SQLException {
    if (connection != null) {
      freeDbConnections.add(connection);
    }
  }
}
