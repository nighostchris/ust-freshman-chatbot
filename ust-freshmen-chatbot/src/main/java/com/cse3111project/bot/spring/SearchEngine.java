package com.cse3111project.bot.spring;

import com.cse3111project.bot.spring.category.Category;
import com.cse3111project.bot.spring.category.transport.*;
import com.cse3111project.bot.spring.category.academic.*;
import com.cse3111project.bot.spring.category.social.*;
// import com.cse3111project.bot.spring.SQLDatabaseEngine;

// import javax.annotation.PostConstruct;
import java.sql.Connection;
// import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLTimeoutException;
import java.sql.SQLException;

import java.net.URISyntaxException;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.StaffNotFoundException;
import com.cse3111project.bot.spring.exception.AmbiguousQueryException;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

public class SearchEngine {
    // conducting search based on userQuery
    // if matched certain QUERY_KEYWORD, reply accordingly using SQLDatabase first
    // if SQLDatabase is failed to load => use the backup static database
	String search(String userQuery){
        String reply = null;  // chatbot reply according to userQuery

        Category categoryResult = null;  // storing search result of user query
        boolean SQLErrorThrown = false;  // flag if exception is thrown while connecting to SQL database

        // parse and manipulate the query
        ArrayList<String> matchedResults = this.parse(userQuery);

        Utilities.arrayLog("matchedResults", matchedResults);

        if (matchedResults.isEmpty())  // if doesn't match any result from the QUERY_KEYWORD list
            return null;  // reply unable to understand what user is asking for

        // if found matched results, find out what category the user is asking for
        try {
            categoryResult = Category.analyze(matchedResults);
        }
        // if results are found, but specified staff is not found on database or 
        // the entire query is ambiguous => reply corresponding message
        catch (StaffNotFoundException | AmbiguousQueryException e) {
            return e.getMessage();
        }

        try {
            if (categoryResult instanceof Transport)
                if (categoryResult instanceof Bus)
                    reply = ((Bus) categoryResult).getArrivalTimeFromKMB();
        }
        catch (Exception e) {
            Utilities.errorLog("unexpected error occurred while reading from KMB database", e);
            return "Unexpected error occurred while reading from KMB database. Sorry";
        }

        try {
            // establish connection to SQL database
            Category.getDatabaseConnection();

            throw new SQLException("*** throwing error to test static database ***");

            // if (categoryResult instanceof Transport){
            //     if (categoryResult instanceof Minibus)
            //         reply = ((Minibus) categoryResult).getArrivalTimeFromSQL();
            //     // ** may add SQL database for Bus **
            // }
            // else if (categoryResult instanceof Academic){
            //     if (categoryResult instanceof Staff)
            //         reply = ((Staff) categoryResult).getContactInfoFromSQL();
            // }
            // else if (categoryResult instanceof Social){
            //     if (categoryResult instanceof Societies)
            //         reply = ((Societies) categoryResult).getSocietyWebsiteFromSQL();
            //     else if (categoryResult instanceof Recreation)
            //         reply = ((Recreation) categoryResult).getBookingInfoFromSQL();
            // }
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
                Category.closeDatabaseConnection();
            }
            catch (SQLException e) {
                Utilities.errorLog("Unable to close database", e);
                // not treated as SQLErrorThrown = true (no need to use static database) since
                // it is successful to access the database beforehand and assign to reply String
                // => just suppress the exception
            }
        }

        try {
            if (SQLErrorThrown){
                if (categoryResult instanceof Transport){
                    if (categoryResult instanceof Minibus)
                        reply = ((Minibus) categoryResult).getArrivalTimeFromStatic();
                    // ** may add static database for Bus **
                    // if (categoryResult instanceof Bus)
                    //     reply = ((Bus) categoryResult).getArrivalTimeFromStatic();
                }
                else if (categoryResult instanceof Academic){
                    if (categoryResult instanceof Staff)
                        reply = ((Staff) categoryResult).getContactInfoFromStatic();
                }
                else if (categoryResult instanceof Social){
                    if (categoryResult instanceof Societies)
                        reply = ((Societies) categoryResult).getSocietyWebsiteFromStatic();
                    else if (categoryResult instanceof Recreation)
                        reply = ((Recreation) categoryResult).getBookingInfoFromStatic();
                }
            }
        }
        // if failed on LAST RESORT ....
        catch (StaticDatabaseFileNotFoundException e) {
            Utilities.errorLog("Static database file not found", e);
            return "***\n1001010100\nOh It seEms I aM bRokEn\n0101010001\n***";
        }

        return reply;
	}

    // symbols needed to be omitted in user query
    // may add Unicode / emojis later **
    public static final String OMITTED_SYMBOLS = "!@#$%^&*()-_=+[]{}\\|:;\'\",<>/?";

    private ArrayList<String> parse(String userQuery){
        StringBuilder queryBuilder = new StringBuilder(userQuery);

        // remove all unneccessary symbols
        for (int symbol = 0; symbol < OMITTED_SYMBOLS.length(); symbol++){
            while (true) {
                int i = queryBuilder.indexOf(new Character(OMITTED_SYMBOLS.charAt(symbol)).toString());
                if (i == -1)  // if symbol not found / all removed
                    break;
                queryBuilder.deleteCharAt(i);
            }
        }

        // assign to the transformed user query text + lower casing
        userQuery = queryBuilder.toString().toLowerCase();

        ArrayList<String> matchedResults = new ArrayList<>();

        // may use Wagner Fischer's algorithm (handle user's typos) in the future
        // => more accurate
        for (String keyword : Category.QUERY_KEYWORD)
            if (userQuery.contains(keyword.toLowerCase()))  // partial match
                matchedResults.add(keyword);

        // detect last name (full name) after STAFF_POSITION_KEYWORD, e.g. Lecturer, Professor, Prof., ...
        Staff.containsLastName(userQuery, matchedResults);

        return matchedResults;
    }
}
