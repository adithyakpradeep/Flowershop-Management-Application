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

public class AdminStockDeleteServlet extends HttpServlet {

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

        // Get the stock ID from the request parameter
        int stockId = Integer.parseInt(request.getParameter("stock_id"));

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            // Prepare the SQL statement to delete the stock
            pst = conn.prepareStatement("DELETE FROM stock_details WHERE stock_id = ?");
            pst.setInt(1, stockId);

            // Execute the update
            pst.executeUpdate();

            // Redirect to the main stock servlet to display the updated table
            response.sendRedirect("AdminStock");

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
    }
}
