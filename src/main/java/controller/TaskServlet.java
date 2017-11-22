package controller;

import com.google.gson.Gson;
import model.Task;
import utility.DbConnection;
import utility.MockData;
import utility.TaskHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/TaskServlet")
public class TaskServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        DbConnection dbConn = DbConnection.getInstance();

        String task = request.getParameter("task");
        String userId = request.getParameter("userId");
        String requiredBy = request.getParameter("requiredBy");
        String priority = request.getParameter("priority");
        String category = request.getParameter("category");
        String id = request.getParameter("id");
        String type = request.getParameter("type");

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = dbConn.getConnect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO task(task, user_id,duedate,priority,category) VALUES ('" + task + "','" + userId + "','" + requiredBy + "','" + priority + "','" + category + "')";
            if (type != null)
            {
                sql = "update task set task = '" + task + "', user_id = '" + userId + "', duedate = '" + requiredBy + "', priority = '" + priority + "', category = '" + category + "' where id='" + id + "'";
            }
            stmt.executeUpdate(sql);
            doGet(request,response);

        } catch (Exception e) {
            pw.println(e);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        DbConnection dbConn = DbConnection.getInstance();
        List<Task> taskList = null;
        String sort = request.getParameter("sort");
        String type = request.getParameter("type");
        if(sort != null){   //sort
            try {
                taskList = TaskHelper.sortTask(sort);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (type != null)   //delete
        {
            Integer id = Integer.parseInt(request.getParameter("id"));
            try {
                TaskHelper.deleteTask(id);
                taskList = dbConn.retrieveData("task");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else { //retrieve

            //Task task = null;
            //List<Task> taskList = null;
            try {
                taskList = dbConn.retrieveData("task");

            } catch (Exception e) {
                out.println(e);
            }

        }
        String JSONtasks;
        //List<Task> taskList = new MockData().retrieveTaskList();
        JSONtasks = new Gson().toJson(taskList);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.write(JSONtasks);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.parseInt(request.getParameter("id"));
        try {
            TaskHelper.deleteTask(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
