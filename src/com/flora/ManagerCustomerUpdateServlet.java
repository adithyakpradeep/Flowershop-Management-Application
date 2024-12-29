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

public class ManagerCustomerUpdateServlet extends HttpServlet {

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

        // Retrieve parameters from the form
        int custId = Integer.parseInt(request.getParameter("custId"));
        String custName = request.getParameter("cust_name");
        String custAddr = request.getParameter("cust_addr");
        String custPlace = request.getParameter("cust_place");
        String custPhone = request.getParameter("cust_phone");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Update the customer details in the database
            pst = conn.prepareStatement(
                "UPDATE customer_details SET cust_name = ?, cust_addr = ?, cust_place = ?, cust_phone = ? WHERE cust_id = ?"
            );
            pst.setString(1, custName);
            pst.setString(2, custAddr);
            pst.setString(3, custPlace);
            pst.setString(4, custPhone);
            pst.setInt(5, custId);

            pst.executeUpdate();

            // Redirect to the main servlet to display the updated customer table
            response.sendRedirect("ManagerCustomer");

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
