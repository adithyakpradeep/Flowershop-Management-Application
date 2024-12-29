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

public class ManagerItemUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;

        // Retrieve form data
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        String itemName = request.getParameter("itemName");
        String itemCode = request.getParameter("itemCode");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Update the item in the database
            pst = conn.prepareStatement(
                "UPDATE item_details SET item_name = ?, item_code = ? WHERE item_id = ?"
            );
            pst.setString(1, itemName);
            pst.setString(2, itemCode);
            pst.setInt(3, itemId);

            pst.executeUpdate();

            // Redirect to the item management page for the manager
            response.sendRedirect("ManagerItem");

        } catch (Exception ex) {
            ex.printStackTrace();
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
