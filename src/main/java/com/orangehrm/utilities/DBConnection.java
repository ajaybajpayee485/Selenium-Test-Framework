package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

	private static final String DB_URL="jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME="root";
	private static final String DB_PASSWORD="";
	
	public static Connection getDBConnection() {
		try {
			System.out.println("startign db connection");
			Connection conn=DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			System.out.println("DB Connection successfull");
			return conn;
		}
		catch(SQLException e) {
			System.out.println("error while establishing the db connection");
			e.printStackTrace();
			return null;
		}
	}
	
	public static Map<String,String> getEmployeeDetails(String employee_id){
		String query="SELECT emp_firstname,emp_lastname from hs_hr_employee where employee_id="+employee_id;
		Map<String,String> employeeDetails=new HashMap<>();
		
		try(Connection conn=getDBConnection();
				Statement stmt=conn.createStatement();
				ResultSet rs=stmt.executeQuery(query)){
			System.out.println("excecuting query"+query);
			if(rs.next()) {
				String firstName=rs.getString("emp_firstname");
				String lastName=rs.getString("emp_lastname");
				
				employeeDetails.put("firstName", firstName);
				employeeDetails.put("lastName", lastName);
			}
			else {
				System.out.println("employed not found");
			}
			
		}
		catch(Exception e) {
			System.out.println("Error while Executing query");
			e.printStackTrace();
		}
		
		return employeeDetails;
	}
	
}
