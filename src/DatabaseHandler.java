import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/weatherap";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "sahal@1234";

    public static boolean insertWeatherData(String location, String temperature, String pressure, String windSpeed,
                                            String humidity) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Create the weather table if it does not exist
            String createTableQuery = "CREATE TABLE IF NOT EXISTS weather (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "location VARCHAR(255)," +
                    "temperature VARCHAR(255)," +
                    "pressure VARCHAR(255)," +
                    "wind_speed VARCHAR(255)," +
                    "humidity VARCHAR(255))";

            try (PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery)) {
                createTableStatement.executeUpdate();
            }

            // Prepare the SQL INSERT query
            String insertQuery = "INSERT INTO weather (location, temperature, pressure, wind_speed, humidity) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                // Set the values in the prepared statement
                preparedStatement.setString(1, location);
                preparedStatement.setString(2, temperature);
                preparedStatement.setString(3, pressure);
                preparedStatement.setString(4, windSpeed);
                preparedStatement.setString(5, humidity);

                // Execute the INSERT query
                preparedStatement.executeUpdate();

                // Data insertion successful, print message and return true
                System.out.println("Data inserted into the database successfully!");
                return true;
            }
        } catch (SQLException e) {
            // Handle any database errors and print the error details
            e.printStackTrace();
            System.err.println("Error inserting data into the database: " + e.getMessage());
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }
}