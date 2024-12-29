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

public class AdminUserDeleteServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final String driver = "com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/flora";
	final String user = "root";
	final String password = "root";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;
        PreparedStatement pst = null;

        int userId = Integer.parseInt(request.getParameter("uId"));
        
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);

            pst = conn.prepareStatement("delete from login_user where user_id=" + userId + "");
            pst.executeUpdate();
            
            request.getRequestDispatcher("AdminUser").forward(request, response);
            
	
	}catch (Exception ex) {
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

