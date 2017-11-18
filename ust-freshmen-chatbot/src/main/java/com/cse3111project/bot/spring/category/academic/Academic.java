package com.cse3111project.bot.spring.category.academic;

import com.cse3111project.bot.spring.category.Category;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.StaffNotFoundException;

/**
 * The Academic abstract class acts as the main controller for coordinating all the user-query on 
 * directory enquiry.
 * @version 1.0
 */
public abstract class Academic extends Category 
{
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Staff.STAFF_NAME_KEYWORD,
                                                                        Staff.STAFF_POSITION_KEYWORD);
                                                                        // Course.QUERY_KEYWORD);

    /**
     * This method will take in useful keywords from user-query and analyze about
     * what sub-category the user is querying on Academic category.
     * @param extractedResults This is the only parameter of the function, which is a 
     * 						   list of processed keyword from the user-query.
     * @return Category This returns the sub-category of which the user-query belongs to
     * @throws StaffNotFoundException Throws when the specific staff is invalid.
     */
    public static Category analyze(final ArrayList<String> extractedResults) throws StaffNotFoundException 
    {
        // extract staff names from extractedResults
        ArrayList<String> queryStaffNameKeywords = new ArrayList<>();
    
        for (String result : extractedResults)
            for (String staffNameKeyword : Staff.STAFF_NAME_KEYWORD)
                if (result.equals(staffNameKeyword))
                    queryStaffNameKeywords.add(staffNameKeyword);

        // later will be changed since Course() has not been implemented yet ***
        return new Staff(queryStaffNameKeywords);
    }
}
