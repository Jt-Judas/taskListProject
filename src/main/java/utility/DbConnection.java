package utility;

import model.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public List<Task> retrieveData(String tableName) throws Exception {
        String readQuery = String.format("SELECT * from %s", tableName);

        try {
            Connection con = getConnect();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(readQuery);

            Task task = null;
            List<Task> taskList = new ArrayList<Task>();


            while (result.next()) {
                task = new Task(Integer.parseInt(result.getString("id")), result.getString("task"), result.getString("user_id"), result.getString("duedate"), result.getString("priority"), result.getString("category"));
                taskList.add(task);
            }

            stmt.close();
            return taskList;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
