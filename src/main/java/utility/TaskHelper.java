package utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import comparator.*;
import model.Task;

public class TaskHelper {

	
	public static List<Task> sortTask(String column) throws Exception {

        DbConnection dbConn = DbConnection.getInstance();
        String readQuery = String.format("SELECT * from %s", "task");

        try {
            Connection con = dbConn.getConnect();
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery(readQuery);

            Task task = null;
            List<Task> taskList = new ArrayList<Task>();


            while (result.next()) {
                task = new Task(Integer.parseInt(result.getString("id")), result.getString("task"), result.getString("user_id"), result.getString("duedate"), result.getString("priority"), result.getString("category"));
                taskList.add(task);
            }
            switch (column.toLowerCase())
            {
            	case "name":
            		//taskList.stream().sorted((object1, object2) -> object1.getTask().compareTo(object2.getTask()));
                    Collections.sort(taskList, new NameComparator());
            		break;
            	case "user id":
            		//taskList.stream().sorted((object1, object2) -> object1.getUserId().compareTo(object2.getUserId()));
                    Collections.sort(taskList,new UserIdComparator());
            		break;
            	case "due":
            		//taskList.stream().sorted((object1, object2) -> object1.getRequiredBy().compareTo(object2.getRequiredBy()));
                    Collections.sort(taskList,new DueComparator());
            		break;
            	case "priority":
            		//taskList.stream().sorted((object1, object2) -> object1.getPriority().compareTo(object2.getPriority()));
                    Collections.sort(taskList,new PriorityComparator());
            		break;
            	case "category":
            		//taskList.stream().sorted((object1, object2) -> object1.getCategory().compareTo(object2.getCategory()));
                    Collections.sort(taskList,new CategoryComparator());
            		break;
        		default:
        			break;
            }
            
            stmt.close();
            return taskList;
        } catch (Exception ex) {
            throw ex;
        }
    }
	
	public void updateTask(Task updatedTask) throws Exception {
		
		DbConnection dbConn = DbConnection.getInstance();
        String updatedQuery = String.format("update Task set task = '" + updatedTask.getTask() + "', userId = '" + updatedTask.getUserId() + "', requiredBy = '" + updatedTask.getRequiredBy() + "', priority = '" + updatedTask.getPriority() + "', category = '" + updatedTask.getCategory() + "'");

        try {
            Connection con = dbConn.getConnect();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(updatedQuery);
            stmt.close();
        } catch (Exception ex) {
            throw ex;
        }
	}   
	
	public void addTask(Task updatedTask) throws Exception {
		
		DbConnection dbConn = DbConnection.getInstance();
		String insertQuery = "INSERT INTO task (task, userId, requiredBy, priority, category)"
				+ "	VALUES (" + updatedTask.getTask() + ", " + updatedTask.getUserId() + ", " + updatedTask.getRequiredBy() + ", " + updatedTask.getPriority() + ", " + updatedTask.getCategory() + ")";
        
        try {
            Connection con = dbConn.getConnect();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(insertQuery);
            stmt.close();
        } catch (Exception ex) {
            throw ex;
        }
	}   
	
	public void deleteTask(int id) throws Exception {
		
		DbConnection dbConn = DbConnection.getInstance();
		String deletedQuery = "DELETE FROM task WHERE id = " + id;
        
        try {
            Connection con = dbConn.getConnect();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(deletedQuery);
            stmt.close();
        } catch (Exception ex) {
            throw ex;
        }
	}

	
}
