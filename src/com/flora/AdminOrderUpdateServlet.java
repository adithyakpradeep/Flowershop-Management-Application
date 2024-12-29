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

public class AdminOrderUpdateServlet extends HttpServlet {

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

        try {
            // Retrieve parameters from the form
            int orderId = Integer.parseInt(request.getParameter("order_id"));
            int itemId = Integer.parseInt(request.getParameter("item_id"));
            int customerId = Integer.parseInt(request.getParameter("cust_id"));
            String orderDate = request.getParameter("order_date");
            int orderQuantity = Integer.parseInt(request.getParameter("order_quantity")); // New quantity field

            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Update the order details in the database, including the order_quantity
            pst = conn.prepareStatement(
                "UPDATE order_details SET item_id = ?, cust_id = ?, order_date = ?, order_quantity = ? WHERE order_id = ?"
            );
            pst.setInt(1, itemId);
            pst.setInt(2, customerId);
            pst.setString(3, orderDate);
            pst.setInt(4, orderQuantity);  // Set the order quantity
            pst.setInt(5, orderId);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("AdminOrder"); // Redirect to display updated order table
            } else {
                response.getWriter().println("<p>Error: Order details could not be updated.</p>");
                response.getWriter().println("<a href='AdminOrder'>Go Back</a>");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().println("<p>Error: " + ex.getMessage() + "</p>");
            response.getWriter().println("<a href='AdminOrder'>Go Back</a>");
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
