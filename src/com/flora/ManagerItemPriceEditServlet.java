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

public class ManagerItemPriceEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database connection details
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
        out.print("<h1 align='center'>Edit Item Price</h1>");

        // Get the item code from the request parameter
        String itemCode = request.getParameter("itemCode");

        if (itemCode == null || itemCode.isEmpty()) {
            out.print("<p>Error: Item Code is missing in the request.</p>");
            out.print("</body></html>");
            return;
        }

        try {
            // Load the database driver
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Query to fetch item details using item_code
            String query = "SELECT id.item_id, id.item_name, ip.price " +
                           "FROM item_details id " +
                           "LEFT JOIN item_price ip ON id.item_id = ip.item_id " +
                           "WHERE id.item_code = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, itemCode);
            rs = pst.executeQuery();

            if (rs.next()) {
                // Extract item details
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                double itemPrice = rs.getDouble("price");

                out.print("<form method='post' action='ManagerItemPriceUpdate'>");

                // Hidden field to pass the item ID
                out.print("<input type='hidden' name='itemId' value='" + itemId + "'/>");

                // Display the current item details
                out.print("<label>Item Code:</label><br>");
                out.print("<input type='text' name='itemCode' value='" + itemCode + "' readonly/><br><br>");

                out.print("<label>Item Name:</label><br>");
                out.print("<input type='text' name='itemName' value='" + itemName + "' readonly/><br><br>");

                out.print("<label>Item Price:</label><br>");
                out.print("<input type='number' step='0.01' name='itemPrice' value='" + itemPrice + "'/><br><br>");

                out.print("<input type='submit' value='Update'/>");
                out.print("</form>");
            } else {
                out.print("<p>Error: No item found for the given Item Code.</p>");
            }

        } catch (Exception ex) {
            out.print("<p>Error: Unable to fetch item details.</p>");
            ex.printStackTrace(out);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sql) {
                sql.printStackTrace(out);
            }
        }

        out.print("</body></html>");
    }
}
