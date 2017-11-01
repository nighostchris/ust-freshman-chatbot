package com.cse3111project.bot.spring.exception;

// thrown when the staff that user queried cannot be found
@SuppressWarnings("serial")  // no need to be Serializable
public class StaffNotFoundException extends Exception {  // Exception implements Serializable
    public StaffNotFoundException(String errMsg){
        super(errMsg);
    }
}
