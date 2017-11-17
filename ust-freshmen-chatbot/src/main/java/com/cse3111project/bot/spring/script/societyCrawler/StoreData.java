package com.cse3111project.bot.spring.societyCrawler;

import com.cse3111project.bot.spring.SQLDatabaseEngine;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.net.URISyntaxException;
import java.sql.Connection;

import java.util.ArrayList;

public class StoreData {
	private ArrayList<Society> societyList;
	private HKUSTSocieties societies;
	
	public StoreData() throws URISyntaxException, SQLException {
		this.societies = new HKUSTSocieties(); // running the crawler by creating the object
		this.societyList = societies.getSocietyList(); // getting the crawled data
	}
	
	public void insertToDatabase() throws URISyntaxException, SQLException {
		SQLDatabaseEngine de = new SQLDatabaseEngine(); // creating this object will get connection to db
		
		// check if the table exists, it will create it if it does not exist
		String createTableSQL = "CREATE TABLE IF NOT EXISTS public.hkust_societies"
				+ "(id int PRIMARY KEY, name varchar(255), website_link varchar(255),"
				+ "CONSTRAINT id_unique UNIQUE (id))";
		PreparedStatement createTable = de.prepare(createTableSQL);
		createTable.executeUpdate();
		createTable.close();
		
		// check if there is conflict, if there is, update the name and website_link
		String insertTableSQL = "INSERT INTO hkust_societies"
				+ "(id,name, website_link) VALUES"
				+ "(?,?,?)"
				+ "ON CONFLICT (id)" 
				+ "DO UPDATE SET (name,website_link) = (EXCLUDED.name,EXCLUDED.website_link);";
		PreparedStatement insertData = de.prepare(insertTableSQL);
		for (int i = 0 ; i < societyList.size(); i++) {
			int id = i+1;
			insertData.setInt(1, id);
			insertData.setString(2, societyList.get(i).getName());
			insertData.setString(3, societyList.get(i).getWebsiteLink());
			insertData.executeUpdate();
		}
		insertData.close();
		de.closeConnection();
	}
	
	// for testing
//	public static void main(String[] args) throws URISyntaxException, SQLException
//	{
//		StoreData test = new StoreData();
//		test.insertToDatabase();
//		System.out.println("FINISHED");
//	}
}
