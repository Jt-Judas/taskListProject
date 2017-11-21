package controller;

import com.google.gson.Gson;
import model.Task;
import utility.DbConnection;
import utility.MockData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
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


        Connection conn;

        Statement stmt = null;
        try {
            conn = dbConn.getConnect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO task(task, user_id,duedate,priority,category) VALUES ('"+ task +"','"+ userId+"','" + requiredBy+ "','" +priority+ "','" + category+"')";
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            pw.println(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String JSONtasks;
        List<Task> taskList = new MockData().retrieveTaskList();
        JSONtasks = new Gson().toJson(taskList);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.write(JSONtasks);
    }
}
