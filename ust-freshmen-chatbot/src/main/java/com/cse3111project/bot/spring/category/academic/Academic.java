package com.cse3111project.bot.spring.category.academic;

import com.cse3111project.bot.spring.category.Category;
import com.cse3111project.bot.spring.category.academic.credit_transfer.CreditTransfer;
import com.cse3111project.bot.spring.category.academic.credit_transfer.ExamCreditTransfer;
import com.cse3111project.bot.spring.category.academic.credit_transfer.NonLocalInstitutionCreditTransfer;
import com.cse3111project.bot.spring.category.academic.credit_transfer.LocalInstitutionCreditTransfer;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.AmbiguousQueryException;

import com.cse3111project.bot.spring.exception.StaffNotFoundException;
import com.cse3111project.bot.spring.exception.CourseNotFoundException;
import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

/**
 * The Academic abstract class acts as the main controller for coordinating all the user-query on 
 * directory enquiry.
 * @version 1.0
 */
public abstract class Academic extends Category 
{
    public static final String QUERY_KEYWORD[]; 
    static {
        QUERY_KEYWORD = Utilities.concatArrays(Staff.STAFF_NAME_KEYWORD,
                                               Staff.STAFF_POSITION_KEYWORD,
                                               CourseWebsiteSearch.WEBSITE_SEARCH_KEYWORD,
                                               CreditTransfer.QUERY_KEYWORD,
                                               ExamCreditTransfer.EXAM_KEYWORD,
                                               NonLocalInstitutionCreditTransfer.INSTITUTION_KEYWORD,
                                               LocalInstitutionCreditTransfer.INSTITUTION_KEYWORD,
                                               ExamCreditTransfer.SUBJECT_KEYWORD,
                                               LocalInstitutionCreditTransfer.SUBJECT_KEYWORD);
        // NonLocalInstitutionCreditTransfer.SUBJECT_KEYWORD would be checked in static method
        // instead of QUERY_KEYWORD since the array is too large
    }

    /**
     * This method will take in useful keywords from user-query and analyze about
     * what sub-category the user is querying on Academic category.
     * @param extractedResults This is the only parameter of the function, which is a 
     * 						   list of processed keyword from the user-query.
     * @return Category This returns the sub-category of which the user-query belongs to
     * @throws StaffNotFoundException Throws when the specific staff is invalid.
     */
    public static Category analyze(final ArrayList<String> extractedResults) throws StaffNotFoundException, CourseNotFoundException, AmbiguousQueryException, StaticDatabaseFileNotFoundException, NotStaticAccessibleError {
        // extract staff names, course codes, or course website keywords from extractedResults
        ArrayList<String> queryStaffNameKeywords = new ArrayList<>();
        ArrayList<String> queryCourseCodeKeywords = new ArrayList<>();
        ArrayList<String> queryCourseWebsiteSearchKeywords = new ArrayList<>();
        ArrayList<String> queryExamCreditTransferKeywords = new ArrayList<>();
        ArrayList<String> queryNonLocalInstitutionCreditTransferKeywords = new ArrayList<>();
        ArrayList<String> queryLocalInstitutionCreditTransferKeywords = new ArrayList<>();
    
        for (String result : extractedResults) 
        {
            if (Utilities.contains(Staff.STAFF_NAME_KEYWORD, result))
                queryStaffNameKeywords.add(result);

            if (Utilities.contains(Course.COURSE_CODE_KEYWORD, result))
                queryCourseCodeKeywords.add(result);
            if (Utilities.contains(CourseWebsiteSearch.WEBSITE_SEARCH_KEYWORD, result))
                queryCourseWebsiteSearchKeywords.add(result);

            if (Utilities.contains(ExamCreditTransfer.SUBJECT_KEYWORD, result))
                queryExamCreditTransferKeywords.add(result);
            if (Utilities.contains(LocalInstitutionCreditTransfer.SUBJECT_KEYWORD, result))
                queryLocalInstitutionCreditTransferKeywords.add(result);
            NonLocalInstitutionCreditTransfer.detectSubject(result, queryNonLocalInstitutionCreditTransferKeywords);
        }

        if (queryStaffNameKeywords.isEmpty() &&
            queryExamCreditTransferKeywords.isEmpty() &&
            queryLocalInstitutionCreditTransferKeywords.isEmpty() &&
            queryNonLocalInstitutionCreditTransferKeywords.isEmpty()) {
            for (String result : extractedResults){
                // querying staff but only given staff position
                if (Utilities.contains(Staff.STAFF_POSITION_KEYWORD, result))
                    throw new StaffNotFoundException("Which staff are you referring to?");
                // only given exam name without subject / course code
                if (Utilities.contains(ExamCreditTransfer.EXAM_KEYWORD, result))
                    throw new AmbiguousQueryException("What subjects have you taken?");
                // only given local / non-local institution name without subject / course code
                if (Utilities.contains(LocalInstitutionCreditTransfer.INSTITUTION_KEYWORD, result) ||
                    Utilities.contains(NonLocalInstitutionCreditTransfer.INSTITUTION_KEYWORD, result))
                    throw new AmbiguousQueryException("Which course would you like to transfer? (Please provide course code)");
            }
        }

        if (Utilities.allEquals(queryStaffNameKeywords.size(), 
                                queryExamCreditTransferKeywords.size(),
                                queryLocalInstitutionCreditTransferKeywords.size(), 
                                queryNonLocalInstitutionCreditTransferKeywords.size()))
            throw new AmbiguousQueryException("I am not quite sure what you are asking for, could you be more clearer?");

        int maximum = Utilities.max(queryStaffNameKeywords.size(),
                                    queryCourseWebsiteSearchKeywords.size(),
                                    queryCourseCodeKeywords.size(),
                                    queryExamCreditTransferKeywords.size(),
                                    queryNonLocalInstitutionCreditTransferKeywords.size(), 
                                    queryLocalInstitutionCreditTransferKeywords.size());

        if (maximum == queryStaffNameKeywords.size())
            return new Staff(queryStaffNameKeywords);
        if (maximum == queryCourseCodeKeywords.size() && 
            queryCourseCodeKeywords.size() == queryCourseWebsiteSearchKeywords.size())
            return new Course(queryCourseCodeKeywords);
        if (maximum == queryCourseCodeKeywords.size())
            return new Course(queryCourseCodeKeywords);
        if (maximum == queryCourseWebsiteSearchKeywords.size())
            return new CourseWebsiteSearch(queryCourseWebsiteSearchKeywords);

        return new CreditTransfer(queryExamCreditTransferKeywords,
                                  queryLocalInstitutionCreditTransferKeywords,
                                  queryNonLocalInstitutionCreditTransferKeywords);
    }
}
