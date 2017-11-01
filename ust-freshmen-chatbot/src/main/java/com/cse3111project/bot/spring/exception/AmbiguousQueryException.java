package com.cse3111project.bot.spring.exception;

// thrown when don't know exactly what user is asking for
// since no need to save a particular state of object => no need to be Serializable
@SuppressWarnings("serial")  // just suppress it
public class AmbiguousQueryException extends Exception {  // Exception implements Serializable
    public AmbiguousQueryException(String errMsg){
        super(errMsg);
    }
}
