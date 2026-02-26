package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	private static final Logger logger = BaseClass.logger;

	public static Connection getDBConnection() {
		try {
			logger.info("Starting DB Connection...");
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			logger.info("DB Connection Successful");
			return conn;
		} catch (SQLException e) {
			logger.error("Error while establishing the DB connection");
			e.printStackTrace();
			return null;
		}
	}

//Get the employee details from DB and store in a map
	public static Map<String, String> getEmployeeDetails(String employeeId) {
		// 1. Use a Prepared Statement placeholder (?)
		String query = "SELECT emp_firstname, emp_middle_name, emp_lastname FROM hs_hr_employee WHERE employee_id = ?";
		Map<String, String> employeeDetails = new HashMap<>();

		try (Connection conn = getDBConnection(); java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {

			// 2. Safely set the parameter
			pstmt.setString(1, employeeId);
			logger.info("Executing query for ID: " + employeeId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					employeeDetails.put("firstName", rs.getString("emp_firstname"));
					employeeDetails.put("middleName",
							rs.getString("emp_middle_name") != null ? rs.getString("emp_middle_name") : "");
					employeeDetails.put("lastName", rs.getString("emp_lastname"));
					logger.info("Employee Data Fetched: " + employeeDetails);
				} else {
					logger.warn("Employee with ID " + employeeId + " not found in DB.");
				}
			}
		} catch (SQLException e) {
			logger.error("Error while executing DB query: " + e.getMessage());
		}
		return employeeDetails;
	}
}
