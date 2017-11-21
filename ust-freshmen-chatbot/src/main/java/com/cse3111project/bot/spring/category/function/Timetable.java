package com.cse3111project.bot.spring.category.function;

import com.cse3111project.bot.spring.utility.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.cse3111project.bot.spring.exception.InvalidDateException;
import com.cse3111project.bot.spring.exception.InvalidTimeslotException;

/**
 * The Timetable abstract class defines the structure for subclass ActivityDB that handles all
 * the user-query on using the Time Manager function.
 * @version 1.0
 */
public abstract class Timetable
{
    public static final String FUNCTION_KEYWORD[] = { "add event", "remove", "display events",
                                                      "display all events" };
    
    /**
     * This method takes no parameter and will return the result for responding user-query
     * to the LINE client. This method supposed to be implemented in subclass ActivityDB.
     */
    public abstract getResult();

    /**
     * This method will take in the original sentence of user-query and determine which method
     * to be called in the subclass ActivityDB.
     * @param extractedResults Original sentence of user-query.
     * @return Category This returns the sub-category of which the user-query belongs to.
     */
    public static Category analyze(final ArrayList<String> extractedResults)
    {
    	/* Fixed format of user-query
    	 Chris wants to [add event of] eat dinner from 6 to 9 on November 22.
    	 Chris wants to [remove] event of eat dinner from 6 to 9 on November 22.
    	 Chris wants to [display events] on November 22.
    	 Chris wants to [display all events].
    	 */
    	if (input.contains("add event"))
		{
			int secondOccur = input.indexOf("to", input.indexOf("to") + 1);
			
			String username = input.substring(0, input.indexOf(' '));
	    	String activityName = input.substring(input.indexOf("of") + 3, input.indexOf("from") - 1);
	    	String startTime = input.substring(input.indexOf("from") + 5, secondOccur - 1);
	    	String endTime = input.substring(secondOccur + 3, input.indexOf("on") - 1);
	    	String month = input.substring(input.indexOf("on") + 3, input.lastIndexOf(" "));
	    	String day = input.substring(input.lastIndexOf(" ") + 1, input.length());
	    	
	    	return new ActivityDB(username, month, day, activityName, startTime, endTime, 1);
		}
		else if (input.contains("remove"))
		{
			int secondOccur = input.indexOf("to", input.indexOf("to") + 1);
			
			String username = input.substring(0, input.indexOf(' '));
	    	String activityName = input.substring(input.indexOf("of") + 3, input.indexOf("from") - 1);
	    	String startTime = input.substring(input.indexOf("from") + 5, secondOccur - 1);
	    	String endTime = input.substring(secondOccur + 3, input.indexOf("on") - 1);
	    	String month = input.substring(input.indexOf("on") + 3, input.lastIndexOf(" "));
	    	String day = input.substring(input.lastIndexOf(" ") + 1, input.length());
			return new ActivityDB(username, month, day, activityName, startTime, endTime, 2);
		}
		else if (input.contains("display events"))
		{
			String month = input.substring(input.indexOfd("on") + 3, input.lastIndexOf(" "));
	    	String day = input.substring(input.lastIndexOf(" ") + 1, input.length());
	    	return new ActivityDB("", month, day, "", "", "", 3);
		}
		else if (input.contains("display all events"))
		{
			return new ActivityDB("", "", "", "", "", "", 4);
		}
    }
    
    public static void returnOriginalSentence(String userQuery, ArrayList<String> matchedResults)
    {
    	matchedResults.add(userQuery);
    }
}
