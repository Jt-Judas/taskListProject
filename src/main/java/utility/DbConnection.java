package utility;

import model.Task;
import model.Team;
import model.User;

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

    public List<Task> retrieveData(String tableName, String userId) throws Exception {
        String readQuery = "";
        if (userId != null) {
            readQuery = String.format("SELECT * from %s where user_id = %s", tableName, userId);
        } else {
            readQuery = String.format("SELECT * from %s", tableName);
        }

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

    public List<Team> retrieveTeams(String tableName) throws Exception {

        String readQuery = String.format("SELECT * from %s", tableName);

        try {
            Connection con = getConnect();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(readQuery);

            Team team = null;
            List<Team> teamListList = new ArrayList<Team>();


            while (result.next()) {
                team = new Team(Integer.parseInt(result.getString("id")), result.getString("name"), result.getString("description"), null);
                teamListList.add(team);
            }

            stmt.close();
            return teamListList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<User> retrieveUSers(String tableName, String teamId) throws Exception {

        String readQuery = "";
        if (teamId != null) {
            readQuery = String.format("SELECT * from %s where team = %s", tableName, teamId);
        } else {
            readQuery = String.format("SELECT * from %s", tableName);
        }

        try {
            Connection con = getConnect();
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            ResultSet result = stmt.executeQuery(readQuery);
            User user = null;
            List<User> userList = new ArrayList<User>();

            while (result.next()) {
                Team team = new Team(Integer.parseInt(result.getString("team")), "", "", null);
                user = new User(Integer.parseInt(result.getString("id")), result.getString("fname"), result.getString("lname"), result.getString("phone"), result.getString("email"), team);

                userList.add(user);
            }

            for (User u : userList) {
                String teamQuery = String.format("SELECT * from team where id = %s", u.getTeam().getId());
                ResultSet teamResult = stmt2.executeQuery(teamQuery);
                Team team = u.getTeam();
                while (teamResult.next()) {
                    team.setName(teamResult.getString("name"));
                    team.setDescription(teamResult.getString("description"));
                    user.setTeam(team);
                }
            }

            stmt.close();
            stmt2.close();
            return userList;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
