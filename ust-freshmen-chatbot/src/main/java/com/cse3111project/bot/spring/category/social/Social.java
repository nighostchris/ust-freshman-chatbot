package com.cse3111project.bot.spring.category.social;

import com.cse3111project.bot.spring.category.Category;

import java.util.ArrayList;

import com.cse3111project.bot.spring.exception.AmbiguousQueryException;

// Social category
// Social
//        -> Societies
public abstract class Social extends Category {
    public static final String QUERY_KEYWORD[] = Societies.QUERY_KEYWORD;

    public static Category query(final ArrayList<String> extractedResults) throws AmbiguousQueryException {
        // will be expanded later
        ArrayList<String> societiesKeywords = new ArrayList<>();

        for (String result : extractedResults)
            for (String societiesKeyword : Societies.SOCIETY_KEYWORD)
                if (result.equals(societiesKeyword))
                    societiesKeywords.add(societiesKeyword);

        // only mentioned UST society, HKUST societies, ... those keyword without 
        // mentioning specific society(ies), e.g. Film Society
        if (societiesKeywords.isEmpty())
            throw new AmbiguousQueryException("There are a variety of UST societies, " + 
                                              "you may check the following link:\n" + Societies.SOCIETIES_LINK);
        return new Societies(societiesKeywords);
    }
}
