package com.cse3111project.bot.spring.exception;

import java.io.FileNotFoundException;

// self-explanatory, thrown if static database file not found 
// (located at ust-freshmen-chatbot/src/main/resources/static)
@SuppressWarnings("serial")  // no need to be Serializable
public class StaticDatabaseFileNotFoundException extends FileNotFoundException {  // FileNotFoundException
                                                                                  // implements Serializable
    public StaticDatabaseFileNotFoundException(String errMsg){
        super(errMsg);
    }
}
