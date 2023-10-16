import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionFactory {
    private static Connection connection = null;
    private ConnectionFactory(){}

    public static Connection getConnection() {
        if (connection == null) {
            ResourceBundle bundle = ResourceBundle.getBundle("database");
            String url = bundle.getString("url"),
            username = bundle.getString("username"),
            password = bundle.getString("password");
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
