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

public class AdminItemDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database connection details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String password = "root";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;

        int itemId = Integer.parseInt(request.getParameter("itemId"));

        try {
            // Load the database driver
            Class.forName(driver);
            // Establish the connection
            conn = DriverManager.getConnection(url, user, password);

            // Prepare the SQL delete statement
            pst = conn.prepareStatement("DELETE FROM item_details WHERE item_id = ?");
            pst.setInt(1, itemId);
            pst.executeUpdate();

            // Forward the request to the AdminItem page
            request.getRequestDispatcher("AdminItem").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                // Close resources
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
    }
}
