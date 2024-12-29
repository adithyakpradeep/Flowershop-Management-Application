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

public class AdminItemDisplayServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/flora";
	final String user = "root";
	final String pass = "root";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection conn = null;
		PreparedStatement pst = null;

		// Retrieve form data
		String itemName = request.getParameter("itemName");
	
		String itemCode = request.getParameter("itemCode");

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pass);

			// Insert the new item into the database
			pst = conn.prepareStatement("INSERT INTO item_details (item_code, item_name) VALUES (?, ?)");
			pst.setString(1, itemCode);
			pst.setString(2, itemName);

			pst.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
				if (conn != null)
					conn.close();
			} catch (SQLException sql) {
				sql.printStackTrace();
			}
		}

		// Redirect to the main servlet to display the updated table
		response.sendRedirect("AdminItem");
	}
}
