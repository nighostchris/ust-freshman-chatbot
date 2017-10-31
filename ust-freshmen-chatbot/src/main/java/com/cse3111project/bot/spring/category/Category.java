package com.cse3111project.bot.spring.category;

import com.cse3111project.bot.spring.category.transport.Transport;
import com.cse3111project.bot.spring.SQLDatabaseEngine;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;

// split the search into categories
// - transport
// --- coming soon ---
public class Category {
    // going to use Utilities.concatArrays() to concatenate all QUERY_KEYWORDs in each catagory
    // as more Categories are defined
    public static final String QUERY_KEYWORD[] = Transport.QUERY_KEYWORD;

    // protected static Connection SQLDatabase = null;
    protected SQLDatabaseEngine SQLDatabase = null;

    protected Category() { }

    // analyze matched results and determine which category the user is questioning for
    public static Category analyze(ArrayList<String> matchedResults){
        // int transportOccurrence = 0;
        // for (String result : matchedResults){
        //     for (String transportKeyword : Transport.QUERY_KEYWORD){
        //         if (result.equals(transportKeyword))
        //             transportOccurrence++;
        //     }
        // }
        // TODO in future:
        // find max of occurrences between categories
        // create a new ArrayList to extract keywords from category having the largest match
        // and pass it to .query()
        // if multiple occurrences are max => ask further

        // modified soon as more Categories are defined
        return Transport.query(matchedResults);
    }

    // setup a connection to SQL database
    public void setConnection(SQLDatabaseEngine SQLDatabase){
        this.SQLDatabase = SQLDatabase;
    }

    // close connection to SQL database
    public void closeDatabaseConnection() throws SQLException {
        if (SQLDatabase != null){  // safe .close()
            SQLDatabase.closeConnection();
            SQLDatabase = null;  // avoid dangling reference
        }
    }
}
