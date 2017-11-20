package com.cse3111project.bot.spring.script.courseDetailsCrawl;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;
import com.cse3111project.bot.spring.model.engine.SQLDatabaseEngine;
import com.cse3111project.bot.spring.model.engine.marker.SQLAccessible;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.net.URISyntaxException;

import java.util.ArrayList;

public class courseDetailsSyncDB implements SQLAccessible{
	private ArrayList<Course> courseList;
	private HKUSTCourse courses;
	
	private courseDetailsSyncDB() throws URISyntaxException, SQLException {
		this.courses = new HKUSTCourse(); // running the crawler by creating the object
		this.courseList = courses.getCourseList(); // getting the crawled data
	}
	
	// this insert function can only be run once as it is an insert SQL
	private void insertToDatabase() throws URISyntaxException, SQLException {
		SQLDatabaseEngine de = new SQLDatabaseEngine(this, "hkust_courses"); // creating this object will get connection to db
		
		// check if the table exists, it will create it if it does not exist
		String createTableSQL = "CREATE TABLE IF NOT EXISTS public.hkust_courses"
				+ "(id SERIAL PRIMARY KEY, course_code varchar(255),"
				+ "course_title varchar(255), course_credit varchar(255),"
				+ "prerequisite text, exclusion text,"
				+ "corequisite text)";
		PreparedStatement createTable = de.prepare(createTableSQL);
		createTable.executeUpdate();
		createTable.close();
		
		String insertTableSQL = "INSERT INTO hkust_courses"
				+ "(course_code, course_title, course_credit, prerequisite, exclusion, corequisite) VALUES"
				+ "(?,?,?,?,?,?);";
		PreparedStatement insertData = de.prepare(insertTableSQL);
		for (int i = 0 ; i < courseList.size(); i++) {
			insertData.setString(1, courseList.get(i).getCourseCode());
			insertData.setString(2, courseList.get(i).getCourseName());
			insertData.setString(3, courseList.get(i).getCredit());
			insertData.setString(4, courseList.get(i).getPrerequisite());
			insertData.setString(5, courseList.get(i).getExclusion());
			insertData.setString(6, courseList.get(i).getCoRequisite());
			insertData.executeUpdate();
		}
		insertData.close();
	}

	// for local testing
//	public static void main(String[] args) throws URISyntaxException, SQLException {
//		courseDetailsSyncDB test = new courseDetailsSyncDB();
//		test.insertToDatabase();
//		System.out.println("FINISHED");
//	}

	@Override
	public String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
