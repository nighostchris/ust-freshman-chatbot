package com.cse3111project.bot.spring.category;

import com.cse3111project.bot.spring.category.transport.Transport;
import com.cse3111project.bot.spring.category.academic.Academic;
import com.cse3111project.bot.spring.category.social.Social;
import com.cse3111project.bot.spring.category.function.Timetable;
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
import com.cse3111project.bot.spring.exception.CourseNotFoundException;
import com.cse3111project.bot.spring.exception.RoomNotFoundException;
import com.cse3111project.bot.spring.exception.CourseNotFoundException;

/**
 * The Category Class classify different features of the chatbot into different categories, act
 * as the framework of classes of features like Transport and Academic. It coordinates the 
 * communication between SearchEngine and different features.
 * @version 1.0
 */
public class Category 
{
    // going to use Utilities.concatArrays() to concatenate all QUERY_KEYWORDs in each catagory
    // as more Categories are defined
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Academic.QUERY_KEYWORD, 
                                                                        Transport.QUERY_KEYWORD,
                                                                        Social.QUERY_KEYWORD,
                                                                        Instruction.QUERY_KEYWORD,
                                                                        Timetable.QUERY_KEYWORD);
    // Campus.QUERY_KEYWORD not enlisted since it only consists of CAMPUS_DIRECTION_KEYWORD
    // which would be handled in CampusETA.detectLocationName() in SearchEngine.parse()

    // not every Category has a SQLDatabase
    // protected static SQLDatabaseEngine SQLDatabase = null;

    /**
     * This method will compare the keywords of user query and the pre-defined keywords of different
     * classes, perform further analyzes and determine which category the user is questioning for
     * @param matchedResults
     * @return
     * @throws AmbiguousQueryException Throws exception upon unclear user-query.
     * @throws StaffNotFoundException Throws exception when the user is asking for details of UST
     * 								  staffs but there is no matching results.
     * @throws RoomNotFoundException Throws exception when the user is asking for estimated time
     * 								 to go from one place to another place within campus, but the
     * 								 room is invalid.
     * @throws MalformedURLException Throws exeception when URL has malform problem.
     * @throws FileNotFoundException Throws exception when static database cannot be loaded.
     * @throws IOException
     */
    public static Category analyze(final List<String> matchedResults) 
           throws AmbiguousQueryException, StaffNotFoundException, CourseNotFoundException,
           		  RoomNotFoundException, MalformedURLException, FileNotFoundException,
           		  IOException 
    {
        ArrayList<String> transportResults = new ArrayList<>();
        ArrayList<String> academicResults = new ArrayList<>();
        ArrayList<String> socialResults = new ArrayList<>();
        ArrayList<String> timetableResults = new ArrayList<>();
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
            for (String timetableKeyword : Timetable.QUERY_KEYWORD)
                if (result.contains(timetableKeyword))
                    timetableResults.add(timetableKeyword);

            // Campus.QUERY_KEYWORD = CampusETA.CAMPUS_DIRECTION_KEYWORD
            for (String campusKeyword : Campus.QUERY_KEYWORD)
                if (result.contains(campusKeyword))  // contains direction keyword "from", "to"
                    campusResults.add(result);
            
            // Instruction.QUERY_KEYWORD = Instruction.QUERY_KEYWORD
            for (String instructionKeyword : Instruction.QUERY_KEYWORD)
            	if (result.equals(instructionKeyword))
            		instructionResults.add(result);
        }

        // each of categories should be UNIQUE, NO query keyword between categories is overlapping
        // functionResults.size() should be placed before academicResults.size()
        // since they have a ridiculously overlap: "TA" and "time table", see partialMatchTimeTable1()
        // ** Update: no need anymore because of the change in .parse()
        int mostMatch = Utilities.max(transportResults.size(), academicResults.size(), 
                                      socialResults.size(), timetableResults.size(), 
                                      campusResults.size(), instructionResults.size());

        if (mostMatch == transportResults.size())
            return Transport.analyze(transportResults);
        if (mostMatch == timetableResults.size())
            return Timetable.analyze(matchedResults);
        if (mostMatch == academicResults.size())
            return Academic.analyze(academicResults);
        if (mostMatch == socialResults.size())
            return Social.analyze(socialResults);
        if (mostMatch == campusResults.size())
            return Campus.analyze(campusResults);
        if (mostMatch == instructionResults.size())
        	return Instruction.analyze(instructionResults);

        throw new AmbiguousQueryException("I am not quite sure what you are talking about, " +
                                          "could you be more clearer? Maybe you want to try out /help");
    }

    /**
     * This method is intended to be overriden by class who implements StaticAccessible, which is used to
     * get data from SQL database if there is any.
     * @return
     * @throws Throwable Throws throwable that indicates this function cannot be used.
     */
    public synchronized String getDataFromSQL() throws Throwable {
        throw new RuntimeException(this.getClass().getName() + ".getDataFromSQL() is not intended to be used");
    }

    /**
     * This method is intended to be overriden by class who implements StaticAccessible, which is used to
     * get data from static database if there is any.
     * @return String This method will return the query result from static database.
     * @throws Throwable Throws throwable that indicates this function cannot be used.
     */
    public synchronized String getDataFromStatic() throws Throwable {
        throw new RuntimeException(this.getClass().getName() + ".getDataFromStatic() is not intended to be used");
    }

    /**
     * This method will take an ArrayList of query result and transform it into a single 
     * String by looping through the list and append each elements to the final display result string.
     * @return String This method will return a single String which is the actual output to the
     * 				  screen of LINE client.
     */
    protected String replyResults(final List<?> results){
        StringBuilder replyBuilder = new StringBuilder("Results:\n");
        int divider = (int)Math.ceil((double)results.size()/5);
        for (int i = 0; i < results.size(); i++){
            replyBuilder.append(results.get(i).toString());
            if (i != results.size() - 1)
                replyBuilder.append("\n");
            if ((i+1)%divider==0 && i != results.size() - 1) {
            	replyBuilder.append("\n");
            }
        }

        return replyBuilder.toString();
    }

    /**
     * This method will take no parameter and just call the toString() method of the class who invoke 
     * this method to get a single string of query-result.
     * @return String This method will return a single String which is the actual output to the
     * 				  screen of LINE client.
     */
    protected String replyResults()
    {
        return this.toString();
    }
}
