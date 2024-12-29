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

public class AdminStockServlet extends HttpServlet {

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
        out.print("<h1 align='center'>Stock Management</h1>");

        // Form for adding or updating stock details
        out.print("<form name='stockForm' method='post' action='AdminStockDisplay' align='center'>");

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

            // Dropdown for Vendor Selection
            out.print("<label>Vendor</label><br>");
            out.print("<select name='ven_id' required>");
            out.print("<option value='' disabled selected>Choose Vendor</option>");
            pst = conn.prepareStatement("SELECT ven_id, ven_name FROM vendor_details");
            rs = pst.executeQuery();

            while (rs.next()) {
                out.print("<option value='" + rs.getInt("ven_id") + "'>" + rs.getString("ven_name") + "</option>");
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

        out.print("<label>Quantity in Kg</label><br>");
        out.print("<input type='number' step='0.01' name='quantity' required><br><br>");
        out.print("<input type='submit' value='Save'>");
        out.print("</form>");
        out.print("<br>");

        // Fetch and display stock details
        int slNo = 1;
        try {
            conn = DriverManager.getConnection(url, user, pass);
            pst = conn.prepareStatement(
                "SELECT stock_details.stock_id, item_details.item_name, vendor_details.ven_name, " +
                "vendor_details.ven_place, stock_details.stock_quantity, stock_details.stock_date " +
                "FROM stock_details " +
                "JOIN item_details ON stock_details.item_id = item_details.item_id " +
                "JOIN vendor_details ON stock_details.ven_id = vendor_details.ven_id"
            );
            rs = pst.executeQuery();

            out.print("<table border='1' align='center'>");
            out.print(
                "<tr><th>SL No</th><th>Item Name</th><th>Vendor Name</th>" +
                "<th>Vendor Place</th><th>Quantity</th><th>Stock Date</th><th>Edit</th><th>Delete</th></tr>"
            );

            while (rs.next()) {
                out.print("<tr>");
                out.print("<td>" + slNo++ + "</td>");
                out.print("<td>" + rs.getString("item_name") + "</td>");
                out.print("<td>" + rs.getString("ven_name") + "</td>");
                out.print("<td>" + rs.getString("ven_place") + "</td>");
                out.print("<td>" + rs.getDouble("stock_quantity") + "</td>");
                out.print("<td>" + rs.getDate("stock_date") + "</td>");
                out.print("<td><a href='AdminStockEdit?stock_id=" + rs.getInt("stock_id") + "'>Edit</a></td>");
                out.print("<td><a href='AdminStockDelete?stock_id=" + rs.getInt("stock_id") + "'>Delete</a></td>");
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
