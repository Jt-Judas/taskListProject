package controller;

import com.google.gson.Gson;
import model.Task;
import model.Team;
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

@WebServlet(name = "TeamServlet", urlPatterns = "/TeamServlet")
public class TeamServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        DbConnection dbConn = DbConnection.getInstance();

        String name = request.getParameter("name");
        String description = request.getParameter("description");

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = dbConn.getConnect();
            stmt = conn.createStatement();

            String sql = "INSERT INTO team(name, description) VALUES ('" + name + "','" + description + "')";
            stmt.executeUpdate(sql);
            doGet(request,response);

        } catch (Exception e) {
            pw.println(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        DbConnection dbConn = DbConnection.getInstance();
        Team team = null;
        List<Team> teamList = null;
        try {
            teamList = dbConn.retrieveTeams("team");

        } catch (Exception e) {
            out.println(e);
        }

        String JSONTeams;
        JSONTeams = new Gson().toJson(teamList);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.write(JSONTeams);
    }
}
