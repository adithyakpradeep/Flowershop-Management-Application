package com.flora;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignInServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    RequestDispatcher dis = null;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String upass = "";
        String uname = "";
        String role = "";

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
            pst = conn.prepareStatement("SELECT user_name, user_password, user_role FROM login_user WHERE user_name = ? AND user_password = ?");
            pst.setString(1, username); // Bind the username
            pst.setString(2, password); // Bind the password

            rs = pst.executeQuery();

            while (rs.next()) {
                uname = rs.getString(1);
                upass = rs.getString(2);
                role = rs.getString(3);
            }

            if (uname.equals(username) && upass.equals(password)) {
                if (role.equals("Admin")) {
                    dis = request.getRequestDispatcher("Adminlanding");
                    dis.forward(request, response);
                } else if (role.equals("Manager")) { 
                    dis = request.getRequestDispatcher("Managerlanding");
                    dis.forward(request, response);
                } else {
                    out.print("Invalid user role");
                    
                }
            } else {
                out.print("User name or password is incorrect");
                dis = request.getRequestDispatcher("index.html");
                dis.include(request, response);
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
    }
}
