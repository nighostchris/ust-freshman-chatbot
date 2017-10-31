package com.cse3111project.bot.spring;

import com.cse3111project.bot.spring.category.Category;
import com.cse3111project.bot.spring.category.transport.*;
import com.cse3111project.bot.spring.SQLDatabaseEngine;

// import javax.annotation.PostConstruct;
import java.sql.Connection;
// import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLTimeoutException;
import java.sql.SQLException;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import lombok.extern.slf4j.Slf4j;  // logging

@Slf4j
public class SearchEngine {
    // conducting search based on userQuery
    // if matched certain QUERY_KEYWORD, reply accordingly using SQLDatabase first
    // if SQLDatabase is failed to load => use the backup static database
	// @Override
	String search(String userQuery){
        String reply = null;  // chatbot reply according to userQuery

        Category categoryResult = null;  // storing search result of user query
        boolean SQLErrorThrown = false;  // flag if exception is thrown while connecting to SQL database

        ArrayList<String> matchedResults = new ArrayList<>();
        // may add Wagner Fischer's algorithm (handle user's typos) in the future
        for (String keyword : Category.QUERY_KEYWORD)
            if (userQuery.toLowerCase().contains(keyword.toLowerCase()))  // partial match
                matchedResults.add(keyword);

        if (matchedResults.isEmpty())  // if doesn't match any result from the QUERY_KEYWORD list
            return null;  // reply unable to understand what user is asking for

        // if found matched results, find out what category the user is asking for
        categoryResult = Category.analyze(matchedResults);

        try {
            // establish connection to SQL database
            categoryResult.setConnection(new SQLDatabaseEngine());

            // throw new SQLException("*** throwing error to test static database ***");

            if (categoryResult instanceof Transport){
                if (categoryResult instanceof Minibus)
                    reply = ((Minibus) categoryResult).getArrivalTimeFromSQL();
                else if (categoryResult instanceof Bus)
                    reply = ((Bus) categoryResult).getArrivalTimeFromSQL();
            }
        }
        // when one of exceptions occurs => load static database
        catch (URISyntaxException e) {  // mostly not happen in practical
            Utilities.errorLog("Database URI cannot be recognized", e);
            SQLErrorThrown = true;
        }
        catch (SQLTimeoutException e) {
            Utilities.errorLog("Database connection timeout", e);
            SQLErrorThrown = true;
        }
        catch (SQLException e) {
            Utilities.errorLog("Unable to connect database", e);
            SQLErrorThrown = true;
        }
        finally {
            try {
                categoryResult.closeDatabaseConnection();
            }
            catch (SQLException e) {
                Utilities.errorLog("Unable to close database", e);
                // not treated as SQLErrorThrown = true (no need to use static database) since
                // it is successful to access the database beforehand and assign to reply String
                // => just suppress the exception
            }
        }

        if (SQLErrorThrown){
            if (categoryResult instanceof Transport){
                if (categoryResult instanceof Minibus)
                    reply = ((Minibus) categoryResult).getArrivalTimeFromStatic();
                if (categoryResult instanceof Bus)
                    reply = ((Bus) categoryResult).getArrivalTimeFromStatic();
            }
        }

        return reply;
	}
}
