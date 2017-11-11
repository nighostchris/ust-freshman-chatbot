package com.cse3111project.bot.spring.directoryEnquiry;

import com.cse3111project.bot.spring.SQLDatabaseEngine;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseSync {
	private ArrayList<Department> departmentList;
	private HKUSTDepartment departments;
	
	private DatabaseSync() throws URISyntaxException, SQLException {
		this.departments = new HKUSTDepartment(); // running the crawler by creating the object
		this.departmentList = departments.getDepartmentList(); // getting the crawled data
	}
	
	// using pattern and matcher to switch around the last name and first name order
	private String switchNameOrder(String oldName) {
		Pattern p = Pattern.compile("[A-Z][A-Z]+");
		Matcher m = p.matcher(oldName);
		String lastName = "";
		while (m.find()) {
            lastName = m.group();
        }
		String firstName = oldName.replaceAll(" " + lastName, "");
		String newName = lastName + " " + firstName;
		return newName;
	}
	
	// this insert function can only be run once as it is an insert SQL
	private void insertToDatabase() throws URISyntaxException, SQLException {
		SQLDatabaseEngine de = new SQLDatabaseEngine(); // creating this object will get connection to db
		
		// check if the table exists, it will create it if it does not exist
		String createTableSQL = "CREATE TABLE IF NOT EXISTS public.hkust_directories"
				+ "(id SERIAL PRIMARY KEY, department varchar(255),"
				+ "name varchar(255), position varchar(255),"
				+ "officeLocation varchar(255), email varchar(255))";
		PreparedStatement createTable = de.prepare(createTableSQL);
		createTable.executeUpdate();
		createTable.close();
		
		String insertTableSQL = "INSERT INTO hkust_directories"
				+ "(department, name, position, officeLocation, email) VALUES"
				+ "(?,?,?,?,?);";
		PreparedStatement insertData = de.prepare(insertTableSQL);
		// for each department, there will be an array of staffs in the StaffList,
		// so will be using nested for loop
		for (int i = 0 ; i < departmentList.size(); i++) {
			ArrayList<Staff> staffList = departmentList.get(i).getStaffList();
			for (int j = 0; j < staffList.size(); j++) {
				insertData.setString(1, departmentList.get(i).getName());
				String newName = switchNameOrder(staffList.get(j).getName());
				insertData.setString(2, newName);
				insertData.setString(3, staffList.get(j).getPosition());
				insertData.setString(4, staffList.get(j).getOfficeLocation());
				insertData.setString(5, staffList.get(j).getEmail());
				insertData.executeUpdate();
			}
		}
		insertData.close();
		de.closeConnection();
	}

	// for testing
//	public static void main(String[] args) throws URISyntaxException, SQLException {
//		DatabaseSync test = new DatabaseSync();
//		test.insertToDatabase();
//		System.out.println("FINISHED");
//	}
}
