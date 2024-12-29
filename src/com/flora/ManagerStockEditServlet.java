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

public class ManagerStockEditServlet extends HttpServlet {

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
        out.print("<h1 align='center'>Edit Stock Details</h1>");

        String stockIdParam = request.getParameter("stock_id");
        if (stockIdParam == null || stockIdParam.trim().isEmpty()) {
            out.print("<p>Error: Stock ID is missing or invalid.</p>");
            out.print("<a href='ManagementStockServlet'>Go Back</a>");
            out.print("</body></html>");
            return;
        }

        int stockId;
        try {
            stockId = Integer.parseInt(stockIdParam);
        } catch (NumberFormatException e) {
            out.print("<p>Error: Stock ID must be a valid number.</p>");
            out.print("<a href='ManagementStockServlet'>Go Back</a>");
            out.print("</body></html>");
            return;
        }

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetch existing stock details
            pst = conn.prepareStatement(
                "SELECT item_id, ven_id, stock_quantity, stock_date FROM stock_details WHERE stock_id = ?"
            );
            pst.setInt(1, stockId);
            rs = pst.executeQuery();

            if (rs.next()) {
                int itemId = rs.getInt("item_id");
                int vendorId = rs.getInt("ven_id");
                double quantity = rs.getDouble("stock_quantity");
                String stockDate = rs.getString("stock_date");

                // Display the edit form
                out.print("<form name='editStockForm' method='post' action='ManagerStockUpdate'>");
                out.print("<input type='hidden' name='stock_id' value='" + stockId + "'>");

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

                // Vendor selection dropdown
                out.print("<label>Vendor</label><br>");
                out.print("<select name='ven_id' required>");
                out.print("<option value='' disabled>Choose Vendor</option>");
                PreparedStatement vendorPst = conn.prepareStatement("SELECT ven_id, ven_name FROM vendor_details");
                ResultSet vendorRs = vendorPst.executeQuery();
                while (vendorRs.next()) {
                    int currentVendorId = vendorRs.getInt("ven_id");
                    String selected = (currentVendorId == vendorId) ? "selected" : "";
                    out.print("<option value='" + currentVendorId + "' " + selected + ">" + vendorRs.getString("ven_name") + "</option>");
                }
                out.print("</select><br><br>");
                vendorRs.close();
                vendorPst.close();

                // Quantity field
                out.print("<label>Quantity in Kg</label><br>");
                out.print("<input type='number' step='0.01' name='stock_quantity' value='" + quantity + "' required><br><br>");

                // Date field
                out.print("<label>Stock Date</label><br>");
                out.print("<input type='date' name='stock_date' value='" + stockDate + "' required><br><br>");

                out.print("<input type='submit' value='Update'>");
                out.print("</form>");
            } else {
                out.print("<p>Error: Stock details not found for the given ID.</p>");
                out.print("<a href='ManagementStockServlet'>Go Back</a>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p>Error occurred while fetching stock details: " + e.getMessage() + "</p>");
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
