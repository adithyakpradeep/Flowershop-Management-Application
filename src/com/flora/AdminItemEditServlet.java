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

public class AdminItemEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html><body>");
        out.print("<h1 align='center'>Edit Item Details</h1>");

        // Get the item ID from the request parameter
        int itemId = Integer.parseInt(request.getParameter("itemId"));

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetch the item's details from the database
            pst = conn.prepareStatement("SELECT * FROM item_details WHERE item_id = ?");
            pst.setInt(1, itemId);
            rs = pst.executeQuery();

            if (rs.next()) {
                out.print("<form method='post' action='AdminItemUpdate'>");

                out.print("<input type='hidden' name='itemId' value='" + rs.getInt("item_id") + "'/>");

                out.print("<label>Item Name:</label><br>");
                out.print("<input type='text' name='itemName' value='" + rs.getString("item_name") + "'/><br><br>");
                
                out.print("<label>Item Code:</label><br>");
                out.print("<input type='text' name='itemCode' value='" + rs.getString("item_code") + "'/><br><br>");

                
                out.print("<input type='submit' value='Update'/>");
                out.print("</form>");
            } else {
                out.print("<p>Item not found.</p>");
            }

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
