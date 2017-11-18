package com.cse3111project.bot.spring.exception;

// self-explanatory, thrown if user-inputed timeslot is invalid
// used in TimeTable sub-application
@SuppressWarnings("serial")  // no need to be Serializable
public class InvalidTimeslotException extends Exception {
    public InvalidTimeslotException(String errMsg){
        super(errMsg);
    }
}
