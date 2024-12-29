package com.flora;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagerUserEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html><body>");
        out.print("<h1 align='center'>Edit User Details</h1>");

        // Get the user ID from the request parameter
        int userId = Integer.parseInt(request.getParameter("uId"));

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetch the user's details from the database
            pst = conn.prepareStatement("SELECT * FROM login_user WHERE user_id = ?");
            pst.setInt(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
                out.print("<form method='post' action='ManagerUserUpdate'>");

                out.print("<input type='hidden' name='uId' value='" + rs.getInt("user_id") + "'/>");

                out.print("<label>User Name:</label>");
                out.print("<input type='text' name='username' value='" + rs.getString("user_name") + "'/><br>");

                out.print("<label>Password:</label>");
                out.print("<input type='password' name='password' value='" + rs.getString("user_password") + "'/><br>");

                out.print("<label>Email:</label>");
                out.print("<input type='text' name='email' value='" + rs.getString("user_email") + "'/><br>");

                out.print("<label>Phone:</label>");
                out.print("<input type='text' name='phone' value='" + rs.getString("user_phone") + "'/><br>");

                out.print("<label>Address:</label><br>");
                out.print("<textarea name='address' rows='4' cols='50'>" + rs.getString("user_addr") + "</textarea><br>");

                out.print("<label>Role:</label>");
                out.print("<select name='role'>");
                out.print("<option" + (rs.getString("user_role").equals("Admin") ? " selected" : "") + ">Admin</option>");
                out.print("<option" + (rs.getString("user_role").equals("Manager") ? " selected" : "") + ">Manager</option>");
                out.print("</select><br>");

                out.print("<input type='submit' value='Update'/>");
                out.print("</form>");
            } else {
                out.print("<p>User not found.</p>");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }

        out.print("</body></html>");
    }
}

