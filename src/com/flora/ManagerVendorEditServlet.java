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

public class ManagerVendorEditServlet extends HttpServlet {

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
        out.print("<h1 align='center'>Edit Vendor Details</h1>");


        // Get the user ID from the request parameter
        int vendorId = Integer.parseInt(request.getParameter("uId"));
        
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);

            // Fetch the user's details from the database
            pst = conn.prepareStatement("SELECT * FROM vendor_details WHERE ven_id = ?");
            pst.setInt(1, vendorId);
            rs = pst.executeQuery();

            if (rs.next()) {
                out.print("<form method='post' action='ManagerVendorUpdate'>");

                out.print("<input type='hidden' name='uId' value='" + rs.getInt("ven_id") + "'/>");

                out.print("<label>Vendor Name:</label>");
                out.print("<input type='text' name='vendorName' value='" + rs.getString("ven_name") + "'/><br>");

                out.print("<label>Address:</label>");
                out.print("<input type='text' name='addr' value='" + rs.getString("ven_addr") + "'/><br>");

                out.print("<label>Place:</label>");
                out.print("<input type='text' name='place' value='" + rs.getString("ven_place") + "'/><br>");

                out.print("<label>Phone:</label>");
                out.print("<input type='text' name='phone' value='" + rs.getString("ven_phone") + "'/><br>");

                out.print("<input type='submit' value='Update'/>");
                out.print("</form>");
                
            } else {
                out.print("<p>Vendor not found.</p>");
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

    

