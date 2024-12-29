package com.flora;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminCustomerUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String stockIdParam = request.getParameter("stock_id");
        String itemIdParam = request.getParameter("item_id");
        String vendorIdParam = request.getParameter("ven_id");
        String quantityParam = request.getParameter("quantity");

        if (stockIdParam == null || itemIdParam == null || vendorIdParam == null || quantityParam == null) {
            out.print("<p>Error: All fields are required.</p>");
            out.print("<a href='AdminStockServlet'>Go Back</a>");
            return;
        }

        try {
            int stockId = Integer.parseInt(stockIdParam);
            int itemId = Integer.parseInt(itemIdParam);
            int vendorId = Integer.parseInt(vendorIdParam);
            double quantity = Double.parseDouble(quantityParam);

            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Update stock details
            pst = conn.prepareStatement(
                "UPDATE stock_details SET item_id = ?, ven_id = ?, stock_quantity = ? WHERE stock_id = ?"
            );
            pst.setInt(1, itemId);
            pst.setInt(2, vendorId);
            pst.setDouble(3, quantity);
            pst.setInt(4, stockId);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                out.print("<p>Stock details updated successfully!</p>");
            } else {
                out.print("<p>Error: Stock details could not be updated.</p>");
            }
            out.print("<a href='AdminStockServlet'>Go Back</a>");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p>Error occurred while updating stock details: " + e.getMessage() + "</p>");
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
