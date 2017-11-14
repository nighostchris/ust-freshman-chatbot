package com.cse3111project.bot.spring.exception;

@SuppressWarnings("serial")
public class RoomNotFoundException extends Exception 
{ 
    public RoomNotFoundException(String errMsg)
    {
        super(errMsg);
    }
}