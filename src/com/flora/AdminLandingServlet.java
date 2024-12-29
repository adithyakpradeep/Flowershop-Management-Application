package com.flora;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminLandingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

       
        out.print("<html><body>");
        out.print("<h1 align='center'>Welcome</h1>");
        out.print("<h1 align='center'>Admin Home</h1>");
        out.print("<form name='adminForm' method='post'>");
        out.print("<table border='1' align='center'>");
        out.print("<tr>");
        out.print("<th colspan='9'>Settings</th>");
        out.print("</tr>");
        out.print("<tr>");
        out.print("<td rowspan='2'><a href='AdminUser'>User Management</a></td>");
        out.print("<td rowspan='2'><a href='AdminVendor'>Vendor</a></td>");
        out.print("<td><a href='AdminItem'>Item</a></td>");
        out.print("<td rowspan='2'><a href='AdminStock'>Stock</a></td>");
        out.print("<td rowspan='2'><a href='AdminCustomer'>Customer</a></td>");
        out.print("<td rowspan='2'><a href='AdminOrder'>Order</a></td>");
        out.print("<td rowspan='2'><a href='Billing'>Billing</a></td>");
        out.print("<td rowspan='2'><a href='Report'>Report</a></td>");
        out.print("<td rowspan='2'><a href='Logout'>Sign Out</a></td>");
        out.print("</tr>");
        out.print("<tr>");
        out.print("<td><a href='AdminItemPrice'>Item Price</a></td>");
        out.print("</tr>");
        out.print("</table>");
        out.print("</form>");
        out.print("</body></html>");
    }
}
