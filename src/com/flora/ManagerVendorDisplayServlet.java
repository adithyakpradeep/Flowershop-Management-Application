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

public class ManagerVendorDisplayServlet  extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/flora";
	final String user = "root";
	final String pass = "root";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection conn = null;
		PreparedStatement pst = null;

		// Retrieve form data
		String vendorName = request.getParameter("vname");
		String address = request.getParameter("addr");
		String place = request.getParameter("place");
		String phone = request.getParameter("phone");

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);

			// Insert the new user into the database
			pst = conn.prepareStatement(
					"INSERT INTO vendor_details (ven_name, ven_addr, ven_place, ven_phone) VALUES (?, ?, ?, ?)");

			pst.setString(1, vendorName);
			pst.setString(2, address);
			pst.setString(3, place);
			pst.setString(4, phone);

			pst.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
			}
		}

		// Redirect to the main servlet to display the updated table
		response.sendRedirect("ManagerVendor");
	}
}

