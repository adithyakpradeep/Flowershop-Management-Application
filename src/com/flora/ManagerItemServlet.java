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

public class ManagerItemServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/flora";
    String user = "root";
    String password = "root";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        int slNo = 1;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.print("<html><body>");
        out.print("<h1 align='center'>Item Management</h1>");

        // Form for adding a new item
        out.print("<form name='managerForm' method='post' action='ManagerItemDisplay' align='center'>");
        out.print("<label>Item Name</label><br>");
        out.print("<input type='text' name='itemName' required><br><br>");

        out.print("<label>Item Code</label><br>");
        out.print("<input type='text' name='itemCode' required><br><br>");

        out.print("<input type='submit' value='Submit'>");
        out.print("</form>");
        out.print("<br>");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            // Display the item table
            pst = conn.prepareStatement("SELECT * FROM item_details");
            rs = pst.executeQuery();

            out.print("<table border='1' align='center'>");
            out.print("<tr><th>SL No</th><th>Item Name</th><th>Item Code</th><th>Edit</th></tr>");

            while (rs.next()) {
                int itemId = rs.getInt("item_id");

                out.print("<tr>");
                out.print("<td>" + slNo + "</td>");
                out.print("<td>" + rs.getString("item_name") + "</td>");
                out.print("<td>" + rs.getString("item_code") + "</td>");
                out.print("<td><a href='ManagerItemEdit?itemId=" + itemId + "'>Edit</a></td>");
                out.print("</tr>");

                slNo++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (pst != null)
                    pst.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
        out.print("</table>");
        out.print("</body></html>");
    }
}
