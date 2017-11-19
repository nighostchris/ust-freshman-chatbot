package com.cse3111project.bot.spring.exception;

// thrown when the staff that user queried cannot be found
@SuppressWarnings("serial")  // no need to be Serializable
public class CourseNotFoundException extends Exception {  // Exception implements Serializable
    public CourseNotFoundException(String errMsg){
        super(errMsg);
    }
}
