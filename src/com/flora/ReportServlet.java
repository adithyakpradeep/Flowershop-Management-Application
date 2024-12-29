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

public class ReportServlet extends HttpServlet {
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
        out.print("<h1 align='center'>Order Report</h1>");

        int slNo = 1;

        try {
            // Establishing connection to the database
            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to fetch all billing details
            pst = conn.prepareStatement(
                "SELECT i.item_name, i.item_code, ip.price AS item_price, od.order_quantity, "
                + "(ip.price * od.order_quantity) AS total_price, c.cust_name, c.cust_addr, c.cust_phone, "
                + "od.order_id, od.order_date, od.item_id, od.cust_id, od.order_quantity "
                + "FROM order_details od "
                + "JOIN item_details i ON od.item_id = i.item_id "
                + "JOIN item_price ip ON i.item_id = ip.item_id "
                + "JOIN customer_details c ON od.cust_id = c.cust_id"
            );

            rs = pst.executeQuery();

            // Displaying the results in an HTML table
            out.print("<table border='1' align='center'>");
            out.print(
                "<tr><th>SL No</th><th>Order ID</th><th>Order Date</th><th>Item ID</th><th>Item Name</th><th>Item Code</th>"
                + "<th>Item Price</th><th>Quantity</th><th>Total Price</th><th>Customer Name</th><th>Customer Address</th><th>Phone</th></tr>"
            );

            // Looping through result set and displaying each row
            while (rs.next()) {
                out.print("<tr>");
                out.print("<td>" + slNo++ + "</td>");
                out.print("<td>" + rs.getInt("order_id") + "</td>");
                out.print("<td>" + rs.getDate("order_date") + "</td>");
                out.print("<td>" + rs.getInt("item_id") + "</td>");
                out.print("<td>" + rs.getString("item_name") + "</td>");
                out.print("<td>" + rs.getString("item_code") + "</td>");

                double itemPrice = rs.getDouble("item_price");
                int orderQuantity = rs.getInt("order_quantity");
                double totalPrice = itemPrice * orderQuantity;

                out.print("<td>" + itemPrice + "</td>");
                out.print("<td>" + orderQuantity + "</td>");
                out.print("<td>" + totalPrice + "</td>");
                out.print("<td>" + rs.getString("cust_name") + "</td>");
                out.print("<td>" + rs.getString("cust_addr") + "</td>");
                out.print("<td>" + rs.getString("cust_phone") + "</td>");
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
