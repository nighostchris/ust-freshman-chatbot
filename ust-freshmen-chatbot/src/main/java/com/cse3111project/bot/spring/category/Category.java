package com.cse3111project.bot.spring.category;

import com.cse3111project.bot.spring.category.transport.Transport;
import com.cse3111project.bot.spring.category.academic.Academic;
import com.cse3111project.bot.spring.category.social.Social;
import com.cse3111project.bot.spring.category.function.Function;

import java.sql.SQLException;

import java.net.URISyntaxException;

import com.cse3111project.bot.spring.SQLDatabaseEngine;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.AmbiguousQueryException;
import com.cse3111project.bot.spring.exception.StaffNotFoundException;

// split the search into categories
// - transport
// - academic
// - social
// --- coming soon ---
public abstract class Category {
    // going to use Utilities.concatArrays() to concatenate all QUERY_KEYWORDs in each catagory
    // as more Categories are defined
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Academic.QUERY_KEYWORD, 
                                                                        Transport.QUERY_KEYWORD,
                                                                        Social.QUERY_KEYWORD,
                                                                        Function.QUERY_KEYWORD);

    // there is only one SQLDatabase deployed => declare static
    protected static SQLDatabaseEngine SQLDatabase = null;

    // analyze partially matched results and determine which category the user is questioning for
    public static Category analyze(final ArrayList<String> matchedResults) 
            throws AmbiguousQueryException, StaffNotFoundException {
        ArrayList<String> transportResults = new ArrayList<>();
        ArrayList<String> academicResults = new ArrayList<>();
        ArrayList<String> socialResults = new ArrayList<>();
        ArrayList<String> functionResults = new ArrayList<>();

        for (String result : matchedResults){
            // Transport.QUERY_KEYWORD = Minibus.QUERY_KEYWORD U Bus.QUERY_KEYWORD
            // U represents union
            for (String transportKeyword : Transport.QUERY_KEYWORD)
                if (result.equals(transportKeyword))
                    transportResults.add(transportKeyword);

            // Academic.QUERY_KEYWORD = Staff.STAFF_NAME_KEYWORD U Staff.STAFF_POSITION_KEYWORD
            for (String academicKeyword : Academic.QUERY_KEYWORD)
                if (result.equals(academicKeyword))
                    academicResults.add(academicKeyword);

            // Social.QUERY_KEYWORD = Societies.QUERY_KEYWORD U Recreation.QUERY_KEYWORD
            for (String socialKeyword : Social.QUERY_KEYWORD)
                if (result.equals(socialKeyword))
                    socialResults.add(socialKeyword);

            // Function.QUERY_KEYWORD = TimeTable.FUNCTION_KEYWORD
            for (String functionKeyword : Function.QUERY_KEYWORD)
                if (result.equals(functionKeyword))
                    functionResults.add(functionKeyword);
        }

        // each of categories should be UNIQUE, NO query keyword between categories is overlapping
        if (transportResults.size() > academicResults.size() && 
            transportResults.size() > socialResults.size() && 
            transportResults.size() > functionResults.size())
            return Transport.query(transportResults);
        else if (academicResults.size() > transportResults.size() && 
                 academicResults.size() > socialResults.size() &&
                 academicResults.size() > functionResults.size())
            return Academic.query(academicResults);
        else if (socialResults.size() > transportResults.size() && 
                 socialResults.size() > academicResults.size() &&
                 socialResults.size() > functionResults.size())
            return Social.query(socialResults);
        else if (functionResults.size() > transportResults.size() && 
                 functionResults.size() > academicResults.size() &&
                 functionResults.size() > socialResults.size())
            return Function.query(functionResults);
        else  // query should not have overlap in a single sentence (untested) ***
            throw new AmbiguousQueryException("I am not quite sure what you are talking about, " +
                                              "could you be more clearer?");
    }

    // extract matchedResults based on what category the user is searching for
    // protected static void extract(ArrayList<String> matchedResults, String category){
    //     if (!category.equals("Transport"))
    //         for (String result : matchedResults)
    //             for (String transportKeyword : Transport.QUERY_KEYWORD)
    //                 if (result.equals(transportKeyword))
    //                     matchedResults.remove(transportKeyword);
    //     else if (!category.equals("Academic"))
    //         for (String result : matchedResults)
    //             for (String academicKeyword : Academic.QUERY_KEYWORD)
    //                 if (result.equals(academicKeyword))
    //                     matchedResults.remove(result);
    // }

    // setup a connection to SQL database
    public static void getDatabaseConnection() throws URISyntaxException, SQLException {
        SQLDatabase = new SQLDatabaseEngine();
    }

    // close connection to SQL database
    public static void closeDatabaseConnection() throws SQLException {
        if (SQLDatabase != null){  // safe .close()
            SQLDatabase.closeConnection();
            SQLDatabase = null;  // avoid dangling reference
        }
    }
}
