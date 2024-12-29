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

public class ManagerOrderUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;

        String orderId = request.getParameter("order_id");
        String itemId = request.getParameter("item_id");
        String custId = request.getParameter("cust_id");
        String orderDate = request.getParameter("order_date");
        String orderQuantity = request.getParameter("order_quantity"); // Retrieve the order_quantity parameter

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Update the order_details table with the new order information, including order_quantity
            pst = conn.prepareStatement(
                "UPDATE order_details SET item_id = ?, cust_id = ?, order_date = ?, order_quantity = ? WHERE order_id = ?"
            );

            pst.setInt(1, Integer.parseInt(itemId));
            pst.setInt(2, Integer.parseInt(custId));
            pst.setString(3, orderDate);
            pst.setInt(4, Integer.parseInt(orderQuantity)); // Set the order_quantity field
            pst.setInt(5, Integer.parseInt(orderId));

            int result = pst.executeUpdate();

            if (result > 0) {
                response.sendRedirect("ManagerOrder"); // Redirect to the order list page
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update order.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
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
