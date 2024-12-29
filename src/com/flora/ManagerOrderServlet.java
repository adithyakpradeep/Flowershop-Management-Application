package com.flora;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManagerOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html><body>");
        out.print("<h1 align='center'>Order Management</h1>");

        // Form for adding new order details
        out.print("<form name='orderForm' method='post' action='ManagerOrderDisplay' align='center'>");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Dropdown for Item Selection
            out.print("<label>Item</label><br>");
            out.print("<select name='item_id' required>");
            out.print("<option value='' disabled selected>Choose Item</option>");
            pst = conn.prepareStatement("SELECT item_id, item_name FROM item_details");
            rs = pst.executeQuery();

            while (rs.next()) {
                out.print("<option value='" + rs.getInt("item_id") + "'>" + rs.getString("item_name") + "</option>");
            }
            out.print("</select><br><br>");
            rs.close();
            pst.close();

            // Dropdown for Customer Selection
            out.print("<label>Customer</label><br>");
            out.print("<select name='cust_id' required>");
            out.print("<option value='' disabled selected>Choose Customer</option>");
            pst = conn.prepareStatement("SELECT cust_id, cust_name FROM customer_details");
            rs = pst.executeQuery();

            while (rs.next()) {
                out.print("<option value='" + rs.getInt("cust_id") + "'>" + rs.getString("cust_name") + "</option>");
            }
            out.print("</select><br><br>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }

        // Add Order Quantity input field
        out.print("<label>Order Quantity in Kg</label><br>");
        out.print("<input type='number' name='order_quantity' required><br><br>");
        out.print("<label>Order Date</label><br>");
        out.print("<input type='date' name='order_date' required><br><br>");
        out.print("<input type='submit' value='Save'>");
        out.print("</form>");
        out.print("<br>");

        // Fetch and display order details with order_quantity
        int slNo = 1;
        try {
            conn = DriverManager.getConnection(url, user, pass);
            pst = conn.prepareStatement(
                "SELECT order_details.order_id, item_details.item_name, customer_details.cust_name, " +
                "order_details.order_date, order_details.order_quantity " +
                "FROM order_details " +
                "JOIN item_details ON order_details.item_id = item_details.item_id " +
                "JOIN customer_details ON order_details.cust_id = customer_details.cust_id"
            );
            rs = pst.executeQuery();

            out.print("<table border='1' align='center'>");
            out.print("<tr><th>SL No</th><th>Item Name</th><th>Customer Name</th><th>Order Date</th><th>Order Quantity</th><th>Edit</th></tr>");

            while (rs.next()) {
                out.print("<tr>");
                out.print("<td>" + slNo++ + "</td>");
                out.print("<td>" + rs.getString("item_name") + "</td>");
                out.print("<td>" + rs.getString("cust_name") + "</td>");
                out.print("<td>" + rs.getDate("order_date") + "</td>");
                out.print("<td>" + rs.getInt("order_quantity") + "</td>"); // Display order_quantity
                out.print("<td><a href='ManagerOrderEdit?order_id=" + rs.getInt("order_id") + "'>Edit</a></td>");
                out.print("</tr>");
            }
            out.print("</table>");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }
        out.print("</body></html>");
    }
}
