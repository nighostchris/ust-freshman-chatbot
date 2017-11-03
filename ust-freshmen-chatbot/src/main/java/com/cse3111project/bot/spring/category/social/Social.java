package com.cse3111project.bot.spring.category.social;

import com.cse3111project.bot.spring.category.Category;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.AmbiguousQueryException;

// Social category
// Social
//        -> Societies
//        -> Recreation
public abstract class Social extends Category {
    public static final String QUERY_KEYWORD[] = Utilities.concatArrays(Societies.QUERY_KEYWORD,
                                                                        Recreation.QUERY_KEYWORD);

    public static Category query(final ArrayList<String> extractedResults) throws AmbiguousQueryException {
        // will be expanded later
        ArrayList<String> societiesKeywords = new ArrayList<>();
        ArrayList<String> recreationKeywords = new ArrayList<>();

        // categorize results
        for (String result : extractedResults){
            for (String societiesKeyword : Societies.SOCIETY_KEYWORD)
                if (result.equals(societiesKeyword))
                    societiesKeywords.add(societiesKeyword);

            for (String recreationKeyword : Recreation.AMENITIES_CATEGORY)
                if (result.equals(recreationKeyword))
                    recreationKeywords.add(recreationKeyword);

            for (String recreationKeyword : Recreation.LIBRARY_KEYWORD)
                if (result.equals(recreationKeyword))
                    recreationKeywords.add(recreationKeyword);
        }

        if (societiesKeywords.size() > recreationKeywords.size())
            return new Societies(societiesKeywords);
        else if (recreationKeywords.size() > societiesKeywords.size())
            return new Recreation(recreationKeywords);
        else if (societiesKeywords.isEmpty() && recreationKeywords.isEmpty()){
            // only mentioned UST society, HKUST societies, ... those broad keyword without 
            // mentioning specific society(ies), e.g. Film Society
            for (String societiesBroadKeyword : Societies.QUERY_KEYWORD)
                if (extractedResults.contains(societiesBroadKeyword))
                    throw new AmbiguousQueryException("There are a variety of UST societies, " + 
                                                      "you may check the following link:\n" + 
                                                      Societies.SOCIETIES_LINK);

            // only mentioned UST amenities, booking, ... those broad keyword without
            // mentioning specific amenity(ies), e.g. Reflection Room
            for (String recreationBroadKeyword : Recreation.QUERY_KEYWORD)
                if (extractedResults.contains(recreationBroadKeyword))
                    throw new AmbiguousQueryException("There are a variety of UST amenities, " +
                                                      "you may check the following links:\n" +
                                                      "SAO amenities booking reference: " + 
                                                      Recreation.AMENITIES_BOOKING_REF + '\n' + 
                                                      "Facilities Booking System: " + 
                                                      Recreation.FBS_LINK + '\n' +
                                                      "Library Room Booking System: " + 
                                                      Recreation.LRBS_LINK);

        }
        // mentioned societiesKeyword, recreationKeyword within ONE sentence
        throw new AmbiguousQueryException("I am not sure what you are asking for, " + 
                                          "could you be more clearer?");
    }
}
