package com.cse3111project.bot.spring.category.campus;

import com.cse3111project.bot.spring.category.Category;

import java.net.MalformedURLException;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;

import com.cse3111project.bot.spring.exception.RoomNotFoundException;

// Campus
//          -> CampusETA
/**
 * 
 * @version 1.0
 *
 */
public abstract class Campus extends Category 
{
    // public static final String QUERY_KEYWORD[] = CampusETA.CAMPUS_DIRECTION_KEYWORD;
    public static final String QUERY_KEYWORD[] = { "from", "to" };

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
