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

public class VendorDeleteServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/flora";
	final String user = "root";
	final String password = "root";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection conn = null;
		PreparedStatement pst = null;

		int vendorId = Integer.parseInt(request.getParameter("uId"));

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);

			pst = conn.prepareStatement("delete from vendor_details where ven_id=" + vendorId + "");
			pst.executeUpdate();

			request.getRequestDispatcher("AdminVendor").forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {

				conn.close();
				pst.close();

			} catch (SQLException sql) {
				sql.printStackTrace();
			}
		}
	}

}
