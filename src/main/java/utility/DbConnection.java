package utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_URL = "jdbc:mysql://localhost/tasklist";

    private String USER = "root";
    private String PASS = "";
    Connection conn = null;

    private static DbConnection instance;

    private DbConnection() {
    }

    public static DbConnection getInstance() {
        if (instance == null)
            instance = new DbConnection();
        return instance;
    }

    public Connection getConnect() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
        return conn;
    }
}
