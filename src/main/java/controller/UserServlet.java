package controller;

import com.google.gson.Gson;
import model.Team;
import model.User;
import utility.DbConnection;

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

@WebServlet(name = "UserServlet",urlPatterns = "/UserServlet")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        DbConnection dbConn = DbConnection.getInstance();

        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String teamID = request.getParameter("team");

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = dbConn.getConnect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO user(fname, lname,phone,email,team) VALUES ('" + fName + "','" + lName + "','" + phone + "','" + email + "','" + teamID + "')";

            stmt.executeUpdate(sql);
            doGet(request,response);

        } catch (Exception e) {
            pw.println(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        DbConnection dbConn = DbConnection.getInstance();
        User user = null;
        List<User> userList = null;
        try {
            userList = dbConn.retrieveUSers("user");

        } catch (Exception e) {
            out.println(e);
        }

        String JSONUsers;
        JSONUsers = new Gson().toJson(userList);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.write(JSONUsers);
    }
}
