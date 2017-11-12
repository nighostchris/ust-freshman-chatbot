package com.cse3111project.bot.spring.exception;

// self-explanatory, thrown if user inputed date is invalid
// used in TimeTable sub-application
@SuppressWarnings("serial")  // no need to be Serializable
public class InvalidDateException extends Exception {
    public InvalidDateException(String errMsg){
        super(errMsg);
    }
}
