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

public class ManagerCustomerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String driver = "com.mysql.cj.jdbc.Driver";
    final String url = "jdbc:mysql://localhost:3306/flora";
    final String user = "root";
    final String pass = "root";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        int slNo = 1;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html><body>");
        out.print("<h1 align='center'>Customer Management </h1>");

        // Form for adding a new customer
        out.print("<form name='customerForm' method='post' action='ManagerCustomerDisplay' align='center'>");
        out.print("<label>Customer Name</label>");
        out.print("<input type='text' name='cust_name' required><br>");

        out.print("<label>Customer Address</label><br>");
        out.print("<textarea name='cust_addr' rows='4' cols='50' required></textarea><br>");

        out.print("<label>Customer Place</label><br>");
        out.print("<input type='text' name='cust_place' required><br>");

        out.print("<label>Customer Phone</label><br>");
        out.print("<input type='text' name='cust_phone' required><br>");

        out.print("<input type='submit' value='Submit'>");
        out.print("</form>");

        out.print("<br>");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Display the customer details table
            pst = conn.prepareStatement("SELECT * FROM customer_details");
            rs = pst.executeQuery();

            out.print("<table border='1' align='center'>");
            out.print("<tr><th>SL No</th><th>Customer Name</th><th>Address</th><th>Place</th><th>Phone</th><th>Edit</th></tr>");
            while (rs.next()) {
                int custId = rs.getInt("cust_id");

                out.print("<tr>");
                out.print("<td>" + slNo + "</td>");
                out.print("<td>" + rs.getString("cust_name") + "</td>");
                out.print("<td>" + rs.getString("cust_addr") + "</td>");
                out.print("<td>" + rs.getString("cust_place") + "</td>");
                out.print("<td>" + rs.getString("cust_phone") + "</td>");
                out.print("<td><a href='ManagerCustomerEdit?custId=" + custId + "'>Edit</a></td>");
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
            } catch (SQLException sql) {
                sql.printStackTrace();
            }
        }

        out.print("</table>");
        out.print("</body></html>");
    }
}
