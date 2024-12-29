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

public class AdminItemPriceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database connection details
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/flora";
    String user = "root";
    String password = "root";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        int slNo = 1;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html><body>");
        out.print("<h1 align='center'>Item Price Management</h1>");

        // Form for adding a new item price
        out.print("<form name='priceForm' method='post' action='AdminItemPriceDisplay' align='center'>");
        out.print("<label>Item Code</label><br>");
        out.print("<input type='text' name='itemCode' required><br><br>");

        out.print("<label>Item Price</label><br>");
        out.print("<input type='text' name='itemPrice' required><br><br>");

        out.print("<input type='submit' value='Submit'>");
        out.print("</form>");
        out.print("<br>");

        try {
            // Load the JDBC driver and connect to the database
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            // Query to fetch item details along with price
            String query = "SELECT id.item_id, id.item_code, id.item_name, ip.price "
                         + "FROM item_details id "
                         + "JOIN item_price ip ON id.item_id = ip.item_id";

            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            // Display the item price table
            out.print("<table border='1' align='center'>");
            out.print("<tr><th>SL No</th><th>Item Code</th><th>Item Name</th><th>Item Price</th><th>Edit</th><th>Delete</th></tr>");

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemCode = rs.getString("item_code");
                String itemName = rs.getString("item_name");
                double itemPrice = rs.getDouble("price");

                out.print("<tr>");
                out.print("<td>" + slNo + "</td>");
                out.print("<td>" + itemCode + "</td>");
                out.print("<td>" + itemName + "</td>");
                out.print("<td>" + itemPrice + "</td>");
                out.print("<td><a href='AdminItemPriceEdit?itemCode=" + itemCode + "'>Edit</a></td>");
                out.print("<td><a href='AdminItemPriceDelete?itemCode=" + itemCode + "'>Delete</a></td>");
                out.print("</tr>");

                slNo++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }

        out.print("</table>");
        out.print("</body></html>");
    }
}
