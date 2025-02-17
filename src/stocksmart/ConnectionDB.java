package stocksmart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/tlapaleria";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public ConnectionDB() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error: Unable to establish connection.");
            e.printStackTrace();
            throw e; // Re-throw the exception for further handling
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
