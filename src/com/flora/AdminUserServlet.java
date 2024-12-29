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

public class AdminUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        int slNo = 1;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html><body>");
        out.print("<h1 align='center'>User Management</h1>");

        // Form for adding a new user
        out.print("<form name='adminForm' method='post' action='AdminUserDisplay' align='center'>");
        out.print("<label>User Name </label><br>");
        out.print("<input type='text' name='username'placeholder='Enter Name' required><br><br>");

        out.print("<label>Password</label><br>");
        out.print("<input type='password' name='password'placeholder='Enter Password' required><br><br>");

        out.print("<label>Email</label><br>");
        out.print("<input type='email' name='email' placeholder='Enter email' required><br><br>");

        out.print("<label>Phone Number</label><br>");
        out.print("<input type='text' name='phone' placeholder='Enter Phone number' required><br><br>");

        out.print("<label>Address</label><br><br>");
        out.print("<textarea name='addr' rows='4' cols='50'placeholder='Enter Address' required></textarea><br><br>");

        out.print("<label>Role</label><br>");
        out.print("<select name='role'>");
        out.print("<option value='Admin'>Admin</option>");
        out.print("<option value='Manager'>Manager</option>");
        out.print("</select><br><br>");

        out.print("<input type='submit' value='Submit'>");
        out.print("</form>");

        out.print("<br>");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Display the user table
            pst = conn.prepareStatement("SELECT * FROM login_user");
            rs = pst.executeQuery();

            out.print("<table border='1' align='center'>");
            out.print(
                    "<tr><th>SL No</th><th>User Name</th><th>Password</th><th>Role</th><th>Email</th><th>Phone</th><th>Address</th><th>Edit</th><th>Delete</th></tr>");
            while (rs.next()) {
                int userId = rs.getInt(1);

                out.print("<tr>");
                out.print("<td>" + slNo + "</td>");
                out.print("<td>" + rs.getString(2) + "</td>");
                out.print("<td>" + rs.getString(3) + "</td>");
                out.print("<td>" + rs.getString(4) + "</td>");
                out.print("<td>" + rs.getString(5) + "</td>");
                out.print("<td>" + rs.getString(6) + "</td>");
                out.print("<td>" + rs.getString(7) + "</td>");
                out.print("<td><a href='AdminUserEdit?uId=" + userId + "'>Edit</a></td>");
                out.print("<td><a href='AdminUserDelete?uId=" + userId + "'>Delete</a></td>");
                out.print("</tr>");

                slNo++;
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

        out.print("</table>");
        out.print("</body></html>");
    }
}
