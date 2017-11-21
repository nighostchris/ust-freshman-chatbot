package com.cse3111project.bot.spring.model.engine.marker;

import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

/**
 * The StaticAccessible Interface implements the structure for class to get data from static database if there is
 * any available to the class.
 * @version 1.0
 */
public interface StaticAccessible 
{
	/**
	 * This abstract method will execute query and get the data from static database. It is supposed
	 * to be implemented in the class that implemented this interface.
	 * @return String This method will return the query result in String format.
	 * @throws NotStaticAccessibleError
	 * @throws StaticDatabaseFileNotFoundException
	 */
    public abstract String getDataFromStatic() throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException;
}
