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

public class AdminCustomerEditServlet extends HttpServlet {

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
        out.print("<h1 align='center'>Edit Customer Details</h1>");

        // Get the customer ID from the request parameter
        int custId = Integer.parseInt(request.getParameter("custId"));

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetch the customer's details from the database
            pst = conn.prepareStatement("SELECT * FROM customer_details WHERE cust_id = ?");
            pst.setInt(1, custId);
            rs = pst.executeQuery();

            if (rs.next()) {
                out.print("<form method='post' action='AdminCustomerUpdate'>");

                out.print("<input type='hidden' name='custId' value='" + rs.getInt("cust_id") + "'/>");

                out.print("<label>Customer Name:</label>");
                out.print("<input type='text' name='cust_name' value='" + rs.getString("cust_name") + "'/><br>");

                out.print("<label>Address:</label><br>");
                out.print("<textarea name='cust_addr' rows='4' cols='50'>" + rs.getString("cust_addr") + "</textarea><br>");

                out.print("<label>Place:</label>");
                out.print("<input type='text' name='cust_place' value='" + rs.getString("cust_place") + "'/><br>");

                out.print("<label>Phone:</label>");
                out.print("<input type='text' name='cust_phone' value='" + rs.getString("cust_phone") + "'/><br>");

                out.print("<input type='submit' value='Update'/>");
                out.print("</form>");
            } else {
                out.print("<p>Customer not found.</p>");
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
