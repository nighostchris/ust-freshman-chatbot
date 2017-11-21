package com.cse3111project.bot.spring.category.function;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;
import com.cse3111project.bot.spring.model.engine.SQLDatabaseEngine;
import com.cse3111project.bot.spring.model.engine.marker.SQLAccessible;

import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

//database structure
//username month day activityName startTime endTime
public class ActivityDB extends Timetable implements SQLAccessible {
	private static final String SQL_TABLE = "time_manager";
	
	private ActivityInfo info;
	private ArrayList<ActivityInfo> results;
	private int option;
	
	public ActivityDB(String username, String month, String day, String activityName,
    				   String startTime, String endTime, int option) throws URISyntaxException, SQLException {
		this.option = option;
		this.info = new ActivityInfo(username, month, day, activityName, startTime, endTime);
		this.createTable();
	}
	
	private void createTable() throws URISyntaxException, SQLException {
		try(SQLDatabaseEngine database = new SQLDatabaseEngine(this, SQL_TABLE)) {
			// check if the table exists, it will create it if it does not exist
			String createTableSQL = "CREATE TABLE IF NOT EXISTS public." + SQL_TABLE
					+ "(id SERIAL PRIMARY KEY, username varchar(255),"
					+ "month varchar(255), day varchar(255),"
					+ "activity_name varchar(255), start_time varchar(255),"
					+ "end_time varchar(255))";
			PreparedStatement createTable = database.prepare(createTableSQL);
			createTable.executeUpdate();
			createTable.close();
		}
	}
	
	public String getResult() throws URISyntaxException, SQLException {
    	if (option == 1)
    		return insertToTable();
    	else if (option == 2)
    		return deleteFromTable();
    	else if (option == 3)
    		return displayEventsAtParticularDate();
    	else
    		return displayAllEvents();
    }
	
	private String insertToTable() throws URISyntaxException, SQLException {
		boolean insert = true;
		for (ActivityInfo activity : results) {
			if (hasConflict(activity)) {
				insert = false;
				break;
			}
		}
		if (insert) {
			try(SQLDatabaseEngine database = new SQLDatabaseEngine(this, SQL_TABLE)) {
				String insertTableSQL = "INSERT INTO " + SQL_TABLE
						+ "(username, month, day, activity_name, start_time, end_time) VALUES"
						+ "(?,?,?,?,?,?);";
				PreparedStatement insertData = database.prepare(insertTableSQL);
				
				insertData.setString(1, info.username);
				insertData.setString(2, info.month);
				insertData.setString(3, info.day);
				insertData.setString(4, info.activityName);
				insertData.setString(5, info.startTime);
				insertData.setString(6, info.endTime);
				insertData.executeUpdate();
				
				insertData.close();
				return "Activity is added.";
			}
		}
		return "Activity has conflict with other activities timeslot.";
	}
	
	private String deleteFromTable() throws URISyntaxException, SQLException {
		try(SQLDatabaseEngine database = new SQLDatabaseEngine(this, SQL_TABLE)) {
			String deleteSQL = "DELETE FROM " + SQL_TABLE
					+ " WHERE username LIKE ? AND month LIKE ? AND day LIKE ?";
			PreparedStatement deleteData = database.prepare(deleteSQL);
			
			deleteData.setString(1, info.username);
			deleteData.setString(2, info.month);
			deleteData.setString(3, info.day);
			deleteData.executeUpdate();
			
			deleteData.close();
			
			return "Activity deleted.";
		}
	}
	
	private String displayEventsAtParticularDate() {
		return super.replyResults(results);
	}
	
	private String displayAllEvents() {
		return super.replyResults(results);
	}
	
	boolean hasConflict(ActivityInfo timeslot)
	{
		if (Integer.parseInt(this.info.startTime) >= Integer.parseInt(timeslot.startTime) && Integer.parseInt(this.info.startTime) < Integer.parseInt(timeslot.endTime))
			return true;
		else if (Integer.parseInt(timeslot.startTime) >= Integer.parseInt(this.info.startTime) && Integer.parseInt(timeslot.startTime) < Integer.parseInt(timeslot.endTime))
			return true;
		else
			return false;
	}
	
	/**
     * This method will connect to the SQL database and retrieve data on details of the expected activity with search
     * on activity parameter(s).
     * @return String 
     * @throws NotSQLAccessibleError
     * @throws SQLException
     */
    @Override
    public synchronized String getDataFromSQL() throws URISyntaxException, NotSQLAccessibleError, SQLException {
        try (SQLDatabaseEngine database = new SQLDatabaseEngine(this, SQL_TABLE)) {
            results = new ArrayList<>();
            
        	StringBuilder SQLStatement = new StringBuilder("SELECT * FROM ")
        			.append(SQL_TABLE).append(" WHERE username LIKE ?");
        	
        	if (option == 1 || option == 2) {
        		SQLStatement.append(" AND month LIKE ? AND day LIKE ?");
        	}
        	else if (option == 3) {
        		SQLStatement.append(" AND month LIKE ?");
        	}
        	PreparedStatement SQLQuery = database.prepare(SQLStatement.toString());
        	if (option == 1 || option == 2) {
        		SQLQuery.setString(1, info.username);
            	SQLQuery.setString(2, info.month);
            	SQLQuery.setString(3, info.day);

        	}
        	else if (option ==3) {
        		SQLQuery.setString(1, info.username);
            	SQLQuery.setString(2, info.month);
        	}
        	else {
        		SQLQuery.setString(1, info.username);
        	}

        	ResultSet reader = database.executeQuery();
        	while (reader.next()){
        		results.add(new ActivityInfo(reader.getString(1), reader.getString(2), 
        									reader.getString(3), reader.getString(4),
        									reader.getString(5), reader.getString(6)));
        	}
        	if (results.isEmpty())
                return "Specified activity not found. Sorry";
        	return this.getResult();
//        	return super.replyResults(results);
        }
    }

    class ActivityInfo {
    	private String username;
        private String month;
        private String day;
        private String activityName;
        private String startTime;
        private String endTime;
        
        private ActivityInfo(String username, String month, String day, String activityName,
        		String startTime, String endTime) {
        	this.username = username;
        	this.month = month;
        	this.day = day;
        	this.activityName = activityName;
        	this.startTime = startTime;
        	this.endTime = endTime;
        }

        @Override
        public String toString() { 
			return  "Username: " + username + '\n' + 
		    		"Activity: " + activityName + '\n' + 
		    		"Date: " + month + " " + day + '\n' + 
		    		"Time: " + startTime + "-" + endTime + '\n';
        }
    }
}