package com.cse3111project.bot.spring;

import com.cse3111project.bot.spring.category.Category;
import com.cse3111project.bot.spring.category.transport.*;
import com.cse3111project.bot.spring.category.academic.*;
import com.cse3111project.bot.spring.category.social.*;
import com.cse3111project.bot.spring.category.function.Function;
import com.cse3111project.bot.spring.category.function.timetable.TimeTable;
import com.cse3111project.bot.spring.category.campus.*;
// import com.cse3111project.bot.spring.SQLDatabaseEngine;

// import javax.annotation.PostConstruct;
import java.sql.Connection;
// import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLTimeoutException;
import java.sql.SQLException;

import java.net.URISyntaxException;

import java.io.IOException;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.StaffNotFoundException;
import com.cse3111project.bot.spring.exception.RoomNotFoundException;
import com.cse3111project.bot.spring.exception.AmbiguousQueryException;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

public class SearchEngine {
    // conducting search based on userQuery
    // if matched certain QUERY_KEYWORD, reply accordingly 
    // Possible response:
    // - general query e.g. finding the office location of staff => return String
    //   query result would be searched on SQL database first
    //   if SQLDatabase is failed to load => use the backup static database
    // - application e.g. TimeTable function => return the application object
	Object search(String userQuery){
        String reply = null;  // chatbot reply according to userQuery

        Category categoryResult = null;  // storing search result of user query
        boolean SQLErrorThrown = false;  // flag if exception is thrown while connecting to SQL database

        // parse and manipulate the query
        ArrayList<String> matchedResults = this.parse(userQuery);

        Utilities.arrayLog("matchedResults", matchedResults);

        // if doesn't match any result from the QUERY_KEYWORD list
        if (matchedResults == null || matchedResults.isEmpty())
            return null;

        // --- Analyzing ---
        // if found matched results, find out what category the user is asking for
        try {
            categoryResult = Category.analyze(matchedResults);
        }
        // if results are found, but specified staff is not found on database or 
        // the entire query is ambiguous => reply corresponding message
        catch (StaffNotFoundException | RoomNotFoundException | AmbiguousQueryException e) {
            return e.getMessage();
        }
        catch (IOException e) {  // MalformedURLException would also be redirected here
            return e.getMessage();
        }

        // --- Application Module ---
        // return the function query object
        if (categoryResult instanceof Function)
            if (categoryResult instanceof TimeTable)
                return categoryResult;

        // --- Web Crawling from pathadvisor.ust.hk ---
        try {
            if (categoryResult instanceof Campus)
                if (categoryResult instanceof CampusETA)
                    return ((CampusETA) categoryResult).getCampusETA();
        }
        catch (IOException e) {  // MalformedURLException would also be redirected here
            return e.getMessage();
        }

        // --- KMB database ---
        try {
            // if search for arrival time of Bus
            // => no need to use SQL database
            // ==> no need to establish connection below to waste time
            if (categoryResult instanceof Transport)
                if (categoryResult instanceof Bus)
                    return ((Bus) categoryResult).getArrivalTimeFromKMB();
        }
        catch (Exception e) {
            Utilities.errorLog("unexpected error occurred while reading from KMB database", e);
            return "Unexpected error occurred while reading from KMB database. Sorry";
        }

        // --- SQL Database ---
        try {
            // establish connection to SQL database
            Category.getDatabaseConnection();

            // throw new SQLException("*** throwing error to test static database ***");

            if (categoryResult instanceof Transport){
                if (categoryResult instanceof Minibus)
                    reply = ((Minibus) categoryResult).getArrivalTimeFromSQL();
                // ** may add SQL database for Bus **
            }
            else if (categoryResult instanceof Academic){
                if (categoryResult instanceof Staff)
                    reply = ((Staff) categoryResult).getContactInfoFromSQL();
            }
            else if (categoryResult instanceof Social){
                if (categoryResult instanceof Societies)
                    reply = ((Societies) categoryResult).getSocietyWebsiteFromSQL();
                else if (categoryResult instanceof Recreation)
                    reply = ((Recreation) categoryResult).getBookingInfoFromSQL();
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
                Category.closeDatabaseConnection();
            }
            catch (SQLException e) {
                Utilities.errorLog("Unable to close database", e);
                // not treated as SQLErrorThrown = true (no need to use static database) since
                // it is successful to access the database beforehand and assign to reply String
                // => just suppress the exception
            }
        }

        // --- static database ---
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
    public static final String OMITTED_SYMBOLS = "!@#$%^&*()_=+[]{}\\|:;\'\",<>/?";

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
        userQuery = queryBuilder.toString();  // some methods need to preserve the casing of userQuery

        ArrayList<String> matchedResults = new ArrayList<>();

        // ** use editDistance() to handle user typos later if have time **
        for (String keyword : Category.QUERY_KEYWORD){
            String userQueryLowerCase = userQuery.toLowerCase();
            String keywordLowerCase = keyword.toLowerCase();
            if (userQueryLowerCase.contains(keywordLowerCase)){  // partial match (match exact substring)
                for (int i = 0; i < Staff.STAFF_POSITION_KEYWORD.length; i++){
                    if (keyword.equals(Staff.STAFF_POSITION_KEYWORD[i])){
                        // if really querying staff (providing staff position)
                        // partial match may match some strange results, e.g. "TA" and "time table"
                        if (Staff.isExactPosition(userQueryLowerCase, keywordLowerCase))
                            matchedResults.add(keyword);
                        break;
                    }
                    else if (i == Staff.STAFF_POSITION_KEYWORD.length - 1)
                        matchedResults.add(keyword);
                }
            }
        }

        Utilities.arrayLog("before .containsLastName()", matchedResults);

        // detect last name (full name) after STAFF_POSITION_KEYWORD, e.g. Lecturer, Professor, Prof., ...
        Staff.containsLastName(userQuery.toLowerCase(), matchedResults);

        // detect location name if provided
        // pass userQuery to preserve casing
        CampusETA.detectLocationName(userQuery, matchedResults);

        return matchedResults;
    }

    // find the minimum edit distance between str1 and str2 
    // (able to handle user typos / resolve partial match strange problem, see partialMatchTimeTable1())
    // using dynamic programming
    // but not applied yet
    private static int editDistance(String str1, String str2){
        // 2D matrix of size (str1.length() + 1) x (str2.length() + 1)
        int dp[][] = new int[str2.length() + 1][str1.length() + 1];

        // base case
        // from str1.substring(0, i) to str2.substring(0, 0) ("")
        // => i deletions
        for (int i = 0; i <= str1.length(); i++)
            dp[0][i] = i;
        // from str1.substring(0, 0) ("") to str2.substring(0, j)
        // => j insertions
        for (int j = 1; j <= str2.length(); j++)
            dp[j][0] = j;

        for (int j = 1; j <= str2.length(); j++){
            for (int i = 1; i <= str1.length(); i++){
                // find the minimum edit distance between str1.substring(0, i - 1) and str2.substring(0, j - 1)
                // if the last character of str1 and str2 are equal
                if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    dp[j][i] = dp[j - 1][i - 1];
                else
                    dp[j][i] = Utilities.min(dp[j - 1][i - 1] + 1,  // replacement cost
                                             dp[j - 1][i] + 1,      // deletion cost
                                             dp[j][i - 1] + 1       // insertion cost
                                            );
            }
        }

        return dp[str2.length()][str1.length()];
    }
}