package scheduler.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone=SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    public static Connection openConnection() {
        if (connection == null) {
            try {
                String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone=SERVER";
                String userName = "sqlUser";
                String password = "Passw0rd!";
                connection = DriverManager.getConnection(jdbcUrl, userName, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Retrieves the existing connection or opens a new connection if one does not exist.
     *
     * @return the active Connection object
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    /**
     * Closes the database connection
     */
    public static void closeConnection() {
       if (connection != null) {
           try {
               connection.close();
               connection = null;
               System.out.println("Connection closed!");
           } catch (Exception e) {
               System.out.println("Error:" + e.getMessage());
       }

        }
    }

}