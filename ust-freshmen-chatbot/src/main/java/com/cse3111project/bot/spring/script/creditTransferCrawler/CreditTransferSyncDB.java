package com.cse3111project.bot.spring.script.creditTransferCrawler;

import com.cse3111project.bot.spring.model.engine.SQLDatabaseEngine;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.net.URISyntaxException;

import java.util.ArrayList;

public class CreditTransferSyncDB {
	private ArrayList<LocalInstitutionCredit> localInstitutionList;
	private ArrayList<ExaminationCredit> examinationList;
	private ArrayList<NonLocalInstitutionCredit> nonLocalInstitutionList;
	private CreditTransfer credits;
	private int option;
	
	private CreditTransferSyncDB() throws URISyntaxException, SQLException {
		this.credits = new CreditTransfer(); // running the crawler by creating the object
		this.option = this.credits.getOption();
		switch(option) {
			case 1:
				this.examinationList = credits.getExaminationList(); // getting the crawled data
				break;
			case 2:
				this.localInstitutionList = credits.getLocalInstitutionList(); // getting the crawled data
				break;
			case 3:
				this.nonLocalInstitutionList = credits.getNonLocalInstitutionList(); // getting the crawled data
		}
	}
	
	private void insertToDatabase() throws URISyntaxException, SQLException {
		
		switch(this.option) {
			case 1:
				SQLDatabaseEngine de = new SQLDatabaseEngine(this, "examinations_credits"); // creating this object will get connection to db
				// check if the table exists, it will create it if it does not exist
				String createExamTableSQL = "CREATE TABLE IF NOT EXISTS public.examinations_credits"
						+ "(id SERIAL PRIMARY KEY, examination varchar(255),"
						+ "subject varchar(255), min_grade varchar(255),"
						+ "transfer_course_code varchar(255), db_reference_no varchar(255))";
				PreparedStatement createExamTable = de.prepare(createExamTableSQL);
				createExamTable.executeUpdate();
				createExamTable.close();
				
				String insertExamTableSQL = "INSERT INTO examinations_credits"
						+ "(examination, subject, min_grade, transfer_course_code, db_reference_no) VALUES"
						+ "(?,?,?,?,?);";
				PreparedStatement insertExamData = de.prepare(insertExamTableSQL);
				for (int i = 0 ; i < examinationList.size(); i++) {
					insertExamData.setString(1, examinationList.get(i).getExamination());
					insertExamData.setString(2, examinationList.get(i).getSubject());
					insertExamData.setString(3, examinationList.get(i).getMinGrade());
					insertExamData.setString(4, examinationList.get(i).getTransferCourseCode());
					insertExamData.setString(5, examinationList.get(i).getDBReferenceNo());
					insertExamData.executeUpdate();
				}
				insertExamData.close();
				break;
			case 2:
				de = new SQLDatabaseEngine(this, "local_institutions_credits"); // creating this object will get connection to db
				// check if the table exists, it will create it if it does not exist
				String createLocalTableSQL = "CREATE TABLE IF NOT EXISTS public.local_institutions_credits"
						+ "(id SERIAL PRIMARY KEY, institution varchar(255),"
						+ "course_code varchar(255), transfer_course_code varchar(255),"
						+ "restriction varchar(255), db_reference_no varchar(255))";
				PreparedStatement createLocalTable = de.prepare(createLocalTableSQL);
				createLocalTable.executeUpdate();
				createLocalTable.close();
				
				String insertLocalTableSQL = "INSERT INTO local_institutions_credits"
						+ "(institution, course_code, transfer_course_code, restriction, db_reference_no) VALUES"
						+ "(?,?,?,?,?);";
				PreparedStatement insertLocalData = de.prepare(insertLocalTableSQL);
				for (int i = 0 ; i < localInstitutionList.size(); i++) {
					insertLocalData.setString(1, localInstitutionList.get(i).getInstitution());
					insertLocalData.setString(2, localInstitutionList.get(i).getCourseCode());
					insertLocalData.setString(3, localInstitutionList.get(i).getTransferCourseCode());
					insertLocalData.setString(4, localInstitutionList.get(i).getRestriction());
					insertLocalData.setString(5, localInstitutionList.get(i).getDBReferenceNo());
					insertLocalData.executeUpdate();
				}
				insertLocalData.close();
				break;
			case 3:
				de = new SQLDatabaseEngine(this, "non_local_institutions_credits"); // creating this object will get connection to db
				// check if the table exists, it will create it if it does not exist
				String createNonLocalTableSQL = "CREATE TABLE IF NOT EXISTS public.non_local_institutions_credits"
						+ "(id SERIAL PRIMARY KEY, country varchar (255), institution varchar(255),"
						+ "course_code varchar(255), transfer_course_code varchar(255),"
						+ "restriction varchar(255), db_reference_no varchar(255))";
				PreparedStatement createNonLocalTable = de.prepare(createNonLocalTableSQL);
				createNonLocalTable.executeUpdate();
				createNonLocalTable.close();
				
				String insertNonLocalTableSQL = "INSERT INTO non_local_institutions_credits"
						+ "(country, institution, course_code, transfer_course_code, restriction, db_reference_no) VALUES"
						+ "(?,?,?,?,?,?);";
				PreparedStatement insertNonLocalData = de.prepare(insertNonLocalTableSQL);
				for (int i = 0 ; i < nonLocalInstitutionList.size(); i++) {
					insertNonLocalData.setString(1, nonLocalInstitutionList.get(i).getCountry());
					insertNonLocalData.setString(2, nonLocalInstitutionList.get(i).getInstitution());
					insertNonLocalData.setString(3, nonLocalInstitutionList.get(i).getCourseCode());
					insertNonLocalData.setString(4, nonLocalInstitutionList.get(i).getTransferCourseCode());
					insertNonLocalData.setString(5, nonLocalInstitutionList.get(i).getRestriction());
					insertNonLocalData.setString(6, nonLocalInstitutionList.get(i).getDBReferenceNo());
					insertNonLocalData.executeUpdate();
				}
				insertNonLocalData.close();
				break;
		}
	}

//	 for testing
//	public static void main(String[] args) throws URISyntaxException, SQLException {
//		CreditTransferSyncDB test = new CreditTransferSyncDB();
//		test.insertToDatabase();
//		System.out.println("FINISHED");
//	}
}
