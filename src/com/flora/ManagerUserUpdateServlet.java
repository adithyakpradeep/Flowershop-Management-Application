package com.flora;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagerUserUpdateServlet  extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;

        int userId = Integer.parseInt(request.getParameter("uId"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String role = request.getParameter("role");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            pst = conn.prepareStatement(
                "UPDATE login_user SET user_name = ?, user_password = ?, user_email = ?, user_phone = ?, user_addr = ?, user_role = ? WHERE user_id = ?"
            );
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, phone);
            pst.setString(5, address);
            pst.setString(6, role);
            pst.setInt(7, userId);

            pst.executeUpdate();

            // Use sendRedirect to send a GET request to AdminUser
            response.sendRedirect("ManagerUser");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                pst.close();
                conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
    }
}

