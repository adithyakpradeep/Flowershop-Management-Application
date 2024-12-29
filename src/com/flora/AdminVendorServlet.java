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

public class AdminVendorServlet extends HttpServlet {

	/**
	 * 
	 */
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
		out.print("<h1 align='center'>Vendor Management</h1>");

		// Form for adding a new user
		out.print("<form name='adminForm' method='post' action='AdminVendorDisplay' align='center'>");
		out.print("<label>Vendor Name</label><br>");
		out.print("<input type='text' name='vname' placeholder='Enter Vendor Name' required><br><br>");

		out.print("<label>Address</label><br>");
		out.print("<textarea name='addr' rows='4' cols='50' placeholder='Enter Address' required></textarea><br><br>");

		out.print("<label>Place</label><br>");
		out.print("<input type='text' name='place' placeholder='Enter Place'  required><br><br>");

		out.print("<label>Phone Number</label><br>");
		out.print("<input type='text' name='phone' placeholder='Enter Phone number' required><br><br>");

		out.print("<input type='submit' value='Submit'>");
		out.print("</form>");

		out.print("<br>");

		try {

			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);

			// Display the user table
			pst = conn.prepareStatement("SELECT * FROM vendor_details");
			rs = pst.executeQuery();

			out.print("<table border='1' align='center'>");
			out.print(
					"<tr><th>SL No</th><th>Vendor Name</th><th>Vendor Address</th><th>Vendor Place</th><th>Vendor Phone</th><th>Edit</th><th>Delete</th></tr>");

			while (rs.next()) {
				int vendorId = rs.getInt(1);

				out.print("<tr>");
				out.print("<td>" + slNo + "</td>");
				out.print("<td>" + rs.getString(2) + "</td>");
				out.print("<td>" + rs.getString(3) + "</td>");
				out.print("<td>" + rs.getString(4) + "</td>");
				out.print("<td>" + rs.getString(5) + "</td>");
				out.print("<td><a href='AdminVendorEdit?uId=" + vendorId + "'>Edit</a></td>");
				out.print("<td><a href='AdminVendorDelete?uId=" + vendorId + "'>Delete</a></td>");
				out.print("</tr>");

				slNo++;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				conn.close();
				pst.close();
				rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		out.print("</table>");
		out.print("</body></html>");
	}

}
