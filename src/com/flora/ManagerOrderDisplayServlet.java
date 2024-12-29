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

public class ManagerOrderDisplayServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;

        // Retrieve form data
        int itemId = Integer.parseInt(request.getParameter("item_id"));
        int customerId = Integer.parseInt(request.getParameter("cust_id"));
        java.sql.Date orderDate = java.sql.Date.valueOf(request.getParameter("order_date")); // Order date from form input
        int orderQuantity = Integer.parseInt(request.getParameter("order_quantity")); // Retrieve order quantity

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Insert the new order record into the database
            pst = conn.prepareStatement(
                "INSERT INTO order_details (item_id, cust_id, order_date, order_quantity) VALUES (?, ?, ?, ?)"
            );
            pst.setInt(1, itemId);
            pst.setInt(2, customerId);
            pst.setDate(3, orderDate);
            pst.setInt(4, orderQuantity); // Insert order quantity

            pst.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }

        // Redirect to the main order servlet to display the updated table
        response.sendRedirect("ManagerOrder");
    }
}
