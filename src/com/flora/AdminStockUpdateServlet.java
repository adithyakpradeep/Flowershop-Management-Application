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

public class AdminStockUpdateServlet extends HttpServlet {

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
            int stockId = Integer.parseInt(request.getParameter("stock_id"));
            int itemId = Integer.parseInt(request.getParameter("item_id"));
            int vendorId = Integer.parseInt(request.getParameter("ven_id"));
            double stockQuantity = Double.parseDouble(request.getParameter("stock_quantity"));
            String stockDate = request.getParameter("stock_date");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Update the stock details in the database
            pst = conn.prepareStatement(
                "UPDATE stock_details SET item_id = ?, ven_id = ?, stock_quantity = ?, stock_date = ? WHERE stock_id = ?"
            );
            pst.setInt(1, itemId);
            pst.setInt(2, vendorId);
            pst.setDouble(3, stockQuantity);
            pst.setString(4, stockDate);
            pst.setInt(5, stockId);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("AdminStock"); // Redirect to display updated stock table
            } else {
                response.getWriter().println("<p>Error: Stock details could not be updated.</p>");
                response.getWriter().println("<a href='AdminStockServlet'>Go Back</a>");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().println("<p>Error: " + ex.getMessage() + "</p>");
            response.getWriter().println("<a href='AdminStockServlet'>Go Back</a>");
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
