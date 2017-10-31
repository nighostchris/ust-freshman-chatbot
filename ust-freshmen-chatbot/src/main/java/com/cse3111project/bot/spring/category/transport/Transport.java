package com.cse3111project.bot.spring.category.transport;

import com.cse3111project.bot.spring.category.Category;

import java.sql.Connection;
import java.sql.SQLException;

import com.cse3111project.bot.spring.utility.Utilities;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class Transport extends Category {
    // compute ESTIMATED arrival time according to currentTime
    protected Calendar currentTime = null;

    // would be used in SQLDatabaseEngine.search()
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Minibus.QUERY_KEYWORD, 
                                                                        Bus.QUERY_KEYWORD);
    // public static final String QUERY_KEYWORD[] = { "estimated", "arrival", "time" };

    public static Category query(final ArrayList<String> extractedResults){
        int minibusOccurrence = 0;
        int busOccurrence = 0;
        for (String result : extractedResults){
            for (String minibusKeyword : Minibus.QUERY_KEYWORD)
                if (result.equals(minibusKeyword))
                    minibusOccurrence++;
            for (String busKeyword : Bus.QUERY_KEYWORD)
                if (result.equals(busKeyword))
                    busOccurrence++;
        }

        // "bus" is subset of "minibus" ("minibus" contains "bus" string)
        // => if search "minibus" -> since use .contains() in SQLDatabaseEngine.search()
        // ==> match "bus", "minibus"
        if (minibusOccurrence >= busOccurrence)
            return new Minibus();  // return user query object

        return new Bus();
    }

    // estimate arrival time from SQL database
    public abstract String getArrivalTimeFromSQL() throws SQLException;

    // if fail to connect SQL database, load the static file to estimate arrival time
    public abstract String getArrivalTimeFromStatic();
}
