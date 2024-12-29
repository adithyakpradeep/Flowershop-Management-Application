package com.flora;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminItemPriceDisplayServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String itemCode = request.getParameter("itemCode");
        String itemPrice = request.getParameter("itemPrice");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetch the item_id using the provided item_code
            String fetchItemIdQuery = "SELECT item_id FROM item_details WHERE item_code = ?";
            pst = conn.prepareStatement(fetchItemIdQuery);
            pst.setString(1, itemCode);
            rs = pst.executeQuery();

            if (!rs.next()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Item Code. Please check and try again.");
                return;
            }

            int itemId = rs.getInt("item_id");

            // Insert the price into the item_price table
            String insertQuery = "INSERT INTO item_price (item_id, price) VALUES (?, ?)";
            pst = conn.prepareStatement(insertQuery);
            pst.setInt(1, itemId);
            pst.setDouble(2, Double.parseDouble(itemPrice));
            pst.executeUpdate();

            // Redirect back to the main servlet
            response.sendRedirect("AdminItemPrice");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
        }
    }

}
