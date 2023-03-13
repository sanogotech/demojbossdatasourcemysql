package com.squins;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class DataSourceServlet extends HttpServlet {


    private DataSource dataSource;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<h1>Hello world!</h1>");
        
        Context envContext;
		try {
	        Context initContext = new InitialContext();
			envContext = (Context) initContext.lookup("java:comp/env");
			dataSource = (DataSource) envContext.lookup("java:/jdbc/books-database");
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
      

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM books");
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {

            while (resultSet.next()) {
                out.println("You have " + resultSet.getInt(1) + " record(s) in your table.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to fetch number of books", e);
        }
    }

}