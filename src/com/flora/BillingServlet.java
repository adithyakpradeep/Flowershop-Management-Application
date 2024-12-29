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

public class BillingServlet extends HttpServlet {

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
        out.print("<h1 align='center'>Billing Details</h1>");

        // Form for selecting Order ID
        out.print("<form name='billingForm' method='post' action='ShowBilling' align='center'>");
        out.print("<label>Select Order ID</label><br>");
        out.print("<select name='order_id' required>");
        out.print("<option value='' disabled selected>Choose Order ID</option>");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetching order_id from the database
            pst = conn.prepareStatement("SELECT order_id FROM order_details");
            rs = pst.executeQuery();

            // Populating the dropdown with order_id options
            while (rs.next()) {
                out.print("<option value='" + rs.getInt("order_id") + "'>" + rs.getInt("order_id") + "</option>");
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

        out.print("<input type='submit' value='Show Billing Details'>");
        out.print("</form>");
        out.print("<br>");

        out.print("</body></html>");
    }
}
