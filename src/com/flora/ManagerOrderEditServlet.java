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

public class ManagerOrderEditServlet extends HttpServlet {

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
        out.print("<h1 align='center'>Edit Order Details</h1>");

        String orderIdParam = request.getParameter("order_id");
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            out.print("<p>Error: Order ID is missing or invalid.</p>");
            out.print("<a href='ManagerOrder'>Go Back</a>");
            out.print("</body></html>");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            out.print("<p>Error: Order ID must be a valid number.</p>");
            out.print("<a href='ManagerOrder'>Go Back</a>");
            out.print("</body></html>");
            return;
        }

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetch existing order details including order_quantity
            pst = conn.prepareStatement(
                "SELECT item_id, cust_id, order_date, order_quantity FROM order_details WHERE order_id = ?"
            );
            pst.setInt(1, orderId);
            rs = pst.executeQuery();

            if (rs.next()) {
                int itemId = rs.getInt("item_id");
                int customerId = rs.getInt("cust_id");
                String orderDate = rs.getString("order_date");
                int orderQuantity = rs.getInt("order_quantity"); // Fetch order_quantity

                // Display the edit form
                out.print("<form name='editOrderForm' method='post' action='ManagerOrderUpdate'>");
                out.print("<input type='hidden' name='order_id' value='" + orderId + "'>");

                // Item selection dropdown
                out.print("<label>Item</label><br>");
                out.print("<select name='item_id' required>");
                out.print("<option value='' disabled>Choose Item</option>");
                PreparedStatement itemPst = conn.prepareStatement("SELECT item_id, item_name FROM item_details");
                ResultSet itemRs = itemPst.executeQuery();
                while (itemRs.next()) {
                    int currentItemId = itemRs.getInt("item_id");
                    String selected = (currentItemId == itemId) ? "selected" : "";
                    out.print("<option value='" + currentItemId + "' " + selected + ">" + itemRs.getString("item_name") + "</option>");
                }
                out.print("</select><br><br>");
                itemRs.close();
                itemPst.close();

                // Customer selection dropdown
                out.print("<label>Customer</label><br>");
                out.print("<select name='cust_id' required>");
                out.print("<option value='' disabled>Choose Customer</option>");
                PreparedStatement custPst = conn.prepareStatement("SELECT cust_id, cust_name FROM customer_details");
                ResultSet custRs = custPst.executeQuery();
                while (custRs.next()) {
                    int currentCustomerId = custRs.getInt("cust_id");
                    String selected = (currentCustomerId == customerId) ? "selected" : "";
                    out.print("<option value='" + currentCustomerId + "' " + selected + ">" + custRs.getString("cust_name") + "</option>");
                }
                out.print("</select><br><br>");
                custRs.close();
                custPst.close();

                // Order date input
                out.print("<label>Order Date</label><br>");
                out.print("<input type='date' name='order_date' value='" + orderDate + "' required><br><br>");

                // Order quantity input (added this part)
                out.print("<label>Order Quantity</label><br>");
                out.print("<input type='number' name='order_quantity' value='" + orderQuantity + "' required><br><br>");

                out.print("<input type='submit' value='Update'>");
                out.print("</form>");
            } else {
                out.print("<p>Order not found.</p>");
            }
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

        out.print("<br><a href='ManagerOrder'>Go Back</a>");
        out.print("</body></html>");
    }
}
