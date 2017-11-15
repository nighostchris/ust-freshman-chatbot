package com.cse3111project.bot.spring.category.transport;

import com.cse3111project.bot.spring.category.Category;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.AmbiguousQueryException;

// Transport
//           -> Minibus
//           -> Bus
public abstract class Transport extends Category {
    // would be used in SearchEngine.search()
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Minibus.QUERY_KEYWORD, 
                                                                        Bus.QUERY_KEYWORD);

    public static Category analyze(final ArrayList<String> extractedResults) throws AmbiguousQueryException {
        ArrayList<String> minibusKeywords = new ArrayList<>();
        ArrayList<String> busRoute91Keywords = new ArrayList<>();
        ArrayList<String> busRoute91MKeywords = new ArrayList<>();

        for (String result : extractedResults){
            for (String minibusKeyword : Minibus.QUERY_KEYWORD)  // only route 11 is collected
                if (result.equals(minibusKeyword))
                    minibusKeywords.add(minibusKeyword);

            for (String busRoute91Keyword : Bus.ROUTE_91_KEYWORD)
                if (result.equals(busRoute91Keyword))
                    busRoute91Keywords.add(busRoute91Keyword);

            for (String busRoute91MKeyword : Bus.ROUTE_91M_KEYWORD)
                if (result.equals(busRoute91MKeyword))
                    busRoute91MKeywords.add(busRoute91MKeyword);
        }

        Utilities.arrayLog("minibusKeywords", minibusKeywords);
        Utilities.arrayLog("busRoute91Keywords", busRoute91Keywords);
        Utilities.arrayLog("busRoute91MKeywords", busRoute91MKeywords);

        // "bus" is subset of "minibus" ("minibus" contains "bus" string)
        // => if search "minibus" -> since use .contains() in SearchEngine.search()
        // ==> match "bus", "minibus"
        if (minibusKeywords.size() >= busRoute91Keywords.size())
            if (minibusKeywords.size() >= busRoute91MKeywords.size())
                return new Minibus();

        // similarly, "91" is subset of "91M"
        // search "91M" => return "91", "91M"
        if (busRoute91MKeywords.size() >= busRoute91Keywords.size()){
            if (extractedResults.contains(Bus.DIRECTION_KEYWORD[Bus.NORTH]))
                return new Bus(Bus.ROUTE_91M, Bus.NORTH);
            else if (extractedResults.contains(Bus.DIRECTION_KEYWORD[Bus.SOUTH]))
                return new Bus(Bus.ROUTE_91M, Bus.SOUTH);
            else
                throw new AmbiguousQueryException("Sorry I don\'t know where you are " +
                                                  "(south or north gate). Please query again.");
        }
        else {
            if (extractedResults.contains(Bus.DIRECTION_KEYWORD[Bus.NORTH]))
                return new Bus(Bus.ROUTE_91, Bus.NORTH);
            else if (extractedResults.contains(Bus.DIRECTION_KEYWORD[Bus.SOUTH]))
                return new Bus(Bus.ROUTE_91, Bus.SOUTH);
            else
                throw new AmbiguousQueryException("Sorry I don\'t know where you are " +
                                                  "(south or north gate). Please query again.");
        }
    }
}
