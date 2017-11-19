package com.cse3111project.bot.spring.category.academic;

import com.cse3111project.bot.spring.category.Category;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.AmbiguousQueryException;

import com.cse3111project.bot.spring.exception.StaffNotFoundException;
import com.cse3111project.bot.spring.exception.CourseNotFoundException;


// Academic
//          -> Staff
//			-> Course
public abstract class Academic extends Category {
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Staff.STAFF_NAME_KEYWORD,
                                                                        Staff.STAFF_POSITION_KEYWORD,
                                                                        Course.COURSE_CODE_KEYWORD);

    // find out which academic category the user is searching for from partially matched results
    public static Category analyze(final ArrayList<String> extractedResults) throws AmbiguousQueryException {
        // extract staff names and course codes from extractedResults
        ArrayList<String> queryStaffNameKeywords = new ArrayList<>();
        ArrayList<String> queryCourseCodeKeywords = new ArrayList<>();
    
        for (String result : extractedResults) {
        	for (String staffNameKeyword : Staff.STAFF_NAME_KEYWORD)
                if (result.equals(staffNameKeyword))
                    queryStaffNameKeywords.add(staffNameKeyword);
        	for (String courseCodeKeyword : Course.COURSE_CODE_KEYWORD)
                if (result.equals(courseCodeKeyword))
                    queryCourseCodeKeywords.add(courseCodeKeyword);
        }
        if (queryStaffNameKeywords.size() > queryCourseCodeKeywords.size())
            return new Staff(queryStaffNameKeywords);
        else if (queryCourseCodeKeywords.size() > queryStaffNameKeywords.size())
            return new Course(queryCourseCodeKeywords);
        
        throw new AmbiguousQueryException("I am not sure what you are asking for, " + 
                "could you be more clearer?");
    }
}
