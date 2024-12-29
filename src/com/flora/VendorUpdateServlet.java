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

public class VendorUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/flora";
	final String user = "root";
	final String pass = "root";

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection conn = null;
		PreparedStatement pst = null;

		int vendorId = Integer.parseInt(request.getParameter("uId"));
		String vendorName = request.getParameter("vendorName");
		String address = request.getParameter("addr");
		String place = request.getParameter("place");
		String phone = request.getParameter("phone");
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);

			pst = conn.prepareStatement("UPDATE vendor_details SET ven_name = ?, ven_addr = ?, ven_place = ?, ven_phone = ? WHERE ven_id = ?");

			pst.setString(1, vendorName);
			pst.setString(2, address);
			pst.setString(3, place);
			pst.setString(4, phone);
			pst.setInt(5, vendorId);

			pst.executeUpdate();

			response.sendRedirect("AdminVendor");

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
	}
}
