package com.cse3111project.bot.spring.model.engine.marker;

import java.sql.SQLException;

import java.net.URISyntaxException;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;

/**
 * The SQLAccessible Interface implements the structure for class to get data from SQL database if there is
 * any available to the class.
 * @version 1.0
 */
public interface SQLAccessible 
{
	/**
	 * This abstract method will execute SQL query at a single thread and get the data from SQL database. It is supposed
	 * to be implemented in the class that implemented this interface.
	 * @return String This method will return the query result in String format
	 * @throws NotSQLAccessibleError
	 * @throws URISyntaxException
	 * @throws SQLException
	 */
    public abstract String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException;
}
