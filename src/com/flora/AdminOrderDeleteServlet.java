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

public class AdminOrderDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String password = "root";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;

        // Get the order ID from the request parameter
        String orderIdParam = request.getParameter("order_id");
        
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            response.getWriter().println("<html><body><p>Error: Order ID is missing or invalid.</p><a href='AdminOrder'>Go Back</a></body></html>");
            return;
        }

        int orderId = 0;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            response.getWriter().println("<html><body><p>Error: Order ID must be a valid number.</p><a href='AdminOrder'>Go Back</a></body></html>");
            return;
        }

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement to delete the order
            pst = conn.prepareStatement("DELETE FROM order_details WHERE order_id = ?");
            pst.setInt(1, orderId);

            // Execute the update
            int rowsAffected = pst.executeUpdate();
            
            if (rowsAffected > 0) {
                // Successful deletion, redirect to the order listing page
                response.sendRedirect("AdminOrder");
            } else {
                // Order ID not found, show error message
                response.getWriter().println("<html><body><p>Error: Order not found.</p><a href='AdminOrder'>Go Back</a></body></html>");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().println("<html><body><p>Error occurred while deleting the order: " + ex.getMessage() + "</p><a href='AdminOrder'>Go Back</a></body></html>");
        } finally {
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
    }
}
