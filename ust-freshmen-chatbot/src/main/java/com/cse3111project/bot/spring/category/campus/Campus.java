package com.cse3111project.bot.spring.category.campus;

import com.cse3111project.bot.spring.category.Category;

import java.net.MalformedURLException;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;

import com.cse3111project.bot.spring.exception.RoomNotFoundException;

/**
 * The Campus abstract class acts as the main controller for coordinating all the
 * user-query on estimated time for one to go from one place to another place within
 * UST campus.
 * @version 1.0
 */
public abstract class Campus extends Category 
{
    // public static final String QUERY_KEYWORD[] = CampusETA.CAMPUS_DIRECTION_KEYWORD;
    public static final String QUERY_KEYWORD[] = { "from", "to" };

    /**
     * This method will take in useful keywords from user-query and analyze about
     * what are the 2 locations that user is querying for estimated time to walk between.
     * @param extractedResults This is the only parameter of the function, which is a 
     * 						   list of processed keyword from the user-query.
     * @return Category This returns a CampusETA object which contains algorithm for calculating the
     * 				    estimated time to walk from 1 place to another place in campus.
     * @throws RoomNotFoundException Throws exception when the room indicated by the user is invalid.
     * @throws MalformedURLException 
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Category analyze(final ArrayList<String> extractedResults) 
            throws RoomNotFoundException, MalformedURLException, FileNotFoundException, IOException
    {
        // extractedResults should merely consist of 
        // "from ...", "to ..."
        // which are extracted in CampusETA.detectLocationName()
        String startPoint = "";
        String endPoint = "";
        for (String result : extractedResults)
            if (result.contains(CampusETA.CAMPUS_DIRECTION_KEYWORD[0]))  // "from ..."
                startPoint = result.split(" ", 2)[1];
        for (String result : extractedResults)
            if (result.contains(CampusETA.CAMPUS_DIRECTION_KEYWORD[1]))  // "to ..."
                endPoint = result.split(" ", 2)[1];

        if (!CampusETA.locationValid(startPoint, endPoint))
            throw new RoomNotFoundException("Room is invalid.");
        
        return new CampusETA(startPoint, endPoint);
    }
}
