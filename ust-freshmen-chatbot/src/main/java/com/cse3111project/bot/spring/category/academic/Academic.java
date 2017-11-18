package com.cse3111project.bot.spring.category.academic;

import com.cse3111project.bot.spring.category.Category;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.StaffNotFoundException;

// Academic
//          -> Staff
public abstract class Academic extends Category {
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Staff.STAFF_NAME_KEYWORD,
                                                                        Staff.STAFF_POSITION_KEYWORD);
                                                                        // Course.QUERY_KEYWORD);

    // find out which academic category the user is searching for from partially matched results
    public static Category analyze(final ArrayList<String> extractedResults) throws StaffNotFoundException {
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
