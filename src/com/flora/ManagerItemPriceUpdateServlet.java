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

public class ManagerItemPriceUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database connection details
    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;

        // Retrieve form data
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        double newPrice = Double.parseDouble(request.getParameter("itemPrice"));

        try {
            // Load the database driver
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Update or insert the item price in the item_price table
            String updateQuery = "INSERT INTO item_price (item_id, price) VALUES (?, ?) " +
                                 "ON DUPLICATE KEY UPDATE price = ?";
            pst = conn.prepareStatement(updateQuery);
            pst.setInt(1, itemId);
            pst.setDouble(2, newPrice);
            pst.setDouble(3, newPrice);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                response.sendRedirect("ManagerItemPrice"); // Redirect to the manager's item price management page
            } else {
                response.getWriter().print("<p>Error: Unable to update the item price.</p>");
            }

        } catch (Exception ex) {
            response.getWriter().print("<p>Error: Unable to update item price.</p>");
            ex.printStackTrace();
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
