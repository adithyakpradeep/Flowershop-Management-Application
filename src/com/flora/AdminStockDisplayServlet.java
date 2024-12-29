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

public class AdminStockDisplayServlet extends HttpServlet {

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
        int vendorId = Integer.parseInt(request.getParameter("ven_id"));
        double stockQuantity = Double.parseDouble(request.getParameter("quantity"));
        java.sql.Date stockDate = new java.sql.Date(System.currentTimeMillis()); // Assuming current date for stock

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Insert the new stock record into the database
            pst = conn.prepareStatement(
                "INSERT INTO stock_details (item_id, ven_id, stock_quantity, stock_date) VALUES (?, ?, ?, ?)"
            );
            pst.setInt(1, itemId);
            pst.setInt(2, vendorId);
            pst.setDouble(3, stockQuantity);
            pst.setDate(4, stockDate);

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

        // Redirect to the main stock servlet to display the updated table
        response.sendRedirect("AdminStock");
    }
}
