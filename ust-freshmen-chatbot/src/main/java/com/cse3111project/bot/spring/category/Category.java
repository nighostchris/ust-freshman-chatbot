package com.cse3111project.bot.spring.category;

import com.cse3111project.bot.spring.category.transport.Transport;
import com.cse3111project.bot.spring.category.academic.Academic;
import com.cse3111project.bot.spring.category.social.Social;
import com.cse3111project.bot.spring.category.function.Function;
import com.cse3111project.bot.spring.category.campus.Campus;
import com.cse3111project.bot.spring.category.instruction.Instruction;

import java.sql.SQLException;

import java.net.URISyntaxException;
import java.net.MalformedURLException;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.AmbiguousQueryException;
import com.cse3111project.bot.spring.exception.StaffNotFoundException;
import com.cse3111project.bot.spring.exception.RoomNotFoundException;

// split the search into categories
// - transport
// - academic
// - social
// - function **
// - campus
// --- coming soon ---
public class Category {
    // going to use Utilities.concatArrays() to concatenate all QUERY_KEYWORDs in each catagory
    // as more Categories are defined
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Academic.QUERY_KEYWORD, 
                                                                        Transport.QUERY_KEYWORD,
                                                                        Social.QUERY_KEYWORD,
                                                                        Function.QUERY_KEYWORD,
                                                                        Instruction.QUERY_KEYWORD);
                                                                        // Campus.QUERY_KEYWORD);
    // Campus.QUERY_KEYWORD not enlisted since it only consists of CAMPUS_DIRECTION_KEYWORD
    // which would be handled in CampusETA.detectLocationName() in SearchEngine.parse()

    // not every Category has a SQLDatabase
    // protected static SQLDatabaseEngine SQLDatabase = null;

    // categorize matched results and determine which category the user is questioning for
    public static Category analyze(final List<String> matchedResults) 
            throws AmbiguousQueryException, StaffNotFoundException, RoomNotFoundException,
                   MalformedURLException, FileNotFoundException, IOException {
        ArrayList<String> transportResults = new ArrayList<>();
        ArrayList<String> academicResults = new ArrayList<>();
        ArrayList<String> socialResults = new ArrayList<>();
        ArrayList<String> functionResults = new ArrayList<>();
        ArrayList<String> campusResults = new ArrayList<>();
        ArrayList<String> instructionResults = new ArrayList<>();

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

            // Campus.QUERY_KEYWORD = CampusETA.CAMPUS_DIRECTION_KEYWORD
            for (String campusKeyword : Campus.QUERY_KEYWORD)
                if (result.contains(campusKeyword))  // contains direction keyword "from", "to"
                    campusResults.add(result);
            
            for (String instructionKeyword : Instruction.QUERY_KEYWORD)
            	if (result.equals(instructionKeyword))
            		instructionResults.add(result);
        }

        // each of categories should be UNIQUE, NO query keyword between categories is overlapping
        // functionResults.size() should be placed before academicResults.size()
        // since they have a ridiculously overlap: "TA" and "time table", see partialMatchTimeTable1()
        // ** Update: no need anymore because of the change in .parse()
        int mostMatch = Utilities.max(transportResults.size(), academicResults.size(), 
                                      socialResults.size(), functionResults.size(), 
                                      campusResults.size(), instructionResults.size());

        if (mostMatch == transportResults.size())
            return Transport.analyze(transportResults);
        if (mostMatch == functionResults.size())
            return Function.analyze(functionResults);
        if (mostMatch == academicResults.size())
            return Academic.analyze(academicResults);
        if (mostMatch == socialResults.size())
            return Social.analyze(socialResults);
        if (mostMatch == campusResults.size())
            return Campus.analyze(campusResults);
        if (mostMatch == instructionResults.size())
        	return Instruction.analyze(instructionResults);

        throw new AmbiguousQueryException("I am not quite sure what you are talking about, " +
                                          "could you be more clearer?\n Maybe you want to try out /help");
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

    // declared here just for visibility (don't want to cast in SearchEngine.search())
    // INTENDED TO BE OVERRIDDEN by class who implements SQLAccessible
    public synchronized String getDataFromSQL() throws Throwable {
        throw new RuntimeException(this.getClass().getName() + ".getDataFromSQL() is not intended to be used");
    }

    // declared here just for visibility (don't want to cast in SearchEngine.search())
    // INTENDED TO BE OVERRIDDEN by class who implements StaticAccessible
    public synchronized String getDataFromStatic() throws Throwable {
        throw new RuntimeException(this.getClass().getName() + ".getDataFromStatic() is not intended to be used");
    }

    // reply a list of results
    protected String replyResults(final List<?> results){
        StringBuilder replyBuilder = new StringBuilder("Results:\n");
        for (int i = 0; i < results.size(); i++){
            replyBuilder.append(results.get(i).toString());
            if (i != results.size() - 1)
                replyBuilder.append("\n");
        }

        return replyBuilder.toString();
    }

    // reply a single result
    protected String replyResults(){
        return this.toString();
    }
}
