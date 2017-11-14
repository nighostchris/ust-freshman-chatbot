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
    // changed later if added Course class **
    public static Category query(final ArrayList<String> extractedResults) throws StaffNotFoundException {
        ArrayList<String> queryStaffNameKeywords = new ArrayList<>();
        ArrayList<String> queryStaffPositionKeywords = new ArrayList<>();
        // ArrayList<String> queryCourseKeywords = new ArrayList<>();

        for (String result : extractedResults){
            // name is more important than position
            // since user may have specified the wrong position of particular staff
            for (String staffPositionKeyword : Staff.STAFF_POSITION_KEYWORD)
                if (result.equals(staffPositionKeyword))
                    queryStaffPositionKeywords.add(staffPositionKeyword);

            for (String staffNameKeyword : Staff.STAFF_NAME_KEYWORD)
                if (result.equals(staffNameKeyword) && !queryStaffNameKeywords.contains(result))
                    queryStaffNameKeywords.add(staffNameKeyword);

            // for (String courseKeyword : Course.QUERY_KEYWORD)
            //     if (result.equals(courseKeyword))
            //         queryCourse.add(courseKeyword);
        }

        Utilities.arrayLog("queryStaffNameKeywords", queryStaffNameKeywords);
        Utilities.arrayLog("queryStaffPositionKeywords", queryStaffPositionKeywords);

        // only position, e.g. Prof., Instructor, Lecturer, ..., is found
        if (queryStaffNameKeywords.isEmpty()){
            StringBuilder errMsgBuilder = new StringBuilder("Sorry the specified ");
            for (int i = 0; i < queryStaffPositionKeywords.size(); i++){
                errMsgBuilder.append(queryStaffPositionKeywords.get(i));
                if (i != queryStaffPositionKeywords.size() - 1)
                    errMsgBuilder.append(", ");
            }
            errMsgBuilder.append(" not found");

            throw new StaffNotFoundException(errMsgBuilder.toString());
        }

        return new Staff(queryStaffNameKeywords);

        // later will be changed since Course() has not been implemented yet ***
        // return new Course();
    }
}
