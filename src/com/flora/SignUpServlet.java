package com.flora;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignUpServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	RequestDispatcher dis = null;

	
	final String driver ="com.mysql.cj.jdbc.Driver";
	final String url = "jdbc:mysql://localhost:3306/flora";
	final String user ="root";
	final String pass = "root";

	
	public void doPost(HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email =request.getParameter("email");
			String phone =request.getParameter("phone");
			String addr = request.getParameter("addr");
			String rol = request.getParameter("role");
			String sql = "INSERT INTO login_user (user_name, user_password, user_email, user_phone, user_addr,user_role) VALUES (?, ?, ?, ?, ?, ?)";
			
		
		try{
			
			Class.forName(driver);
			conn =DriverManager.getConnection(url,user,pass);
			
			
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, phone);
            pst.setString(5, addr);
            pst.setString(6, rol);

            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
            	dis=request.getRequestDispatcher("index.html");
				dis.forward(request, response);
            } else {
                out.print("<html><body>");
                out.print("<h1>Signup Failed</h1>");
                out.print("</body></html>");
            }

			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				
			conn.close();
			pst.close();
			rs.close();
			
			}catch(SQLException sqle){
				sqle.printStackTrace();
			}	
		}
	}

}

	
	
	

	
	