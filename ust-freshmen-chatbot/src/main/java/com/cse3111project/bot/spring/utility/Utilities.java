package com.cse3111project.bot.spring.utility;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * The Utilities class apply the strategy pattern and group up all general functions that will be
 * called frequently by all classes as a family of algorithms.
 * @version 1.0
 */
@Slf4j
public final class Utilities 
{
	/**
	 * Constructor of Utilities class. It was made private to make sure no one could instantiate Utilities 
	 * object since all methods here would be static.
	 */
    private Utilities() { }

    /**
     * This method is used to concatenate two arrays. Time Complexity: O(sum of arrays length)
     * @param arrays Only parameter of this method, representing all the String arrays to be concatenated.
     * @return String[] Return the concatenated String array.
     */
    public static String[] concatArrays(String[]... arrays)
    {
        int newArrSize = 0;
        for (int i = 0; i < arrays.length; i++)
            newArrSize += arrays[i].length;

        String newArr[] = new String[newArrSize];

        int k = 0;
        for (int i = 0; i < arrays.length; i++)
            for (int j = 0; j < arrays[i].length; j++, k++)
                newArr[k] = arrays[i][j];

        return newArr;
    }

    /**
     * This method will find the maximum object within the input arguments. Time Complexity: O(args.length)
     * @param args This method will take in infinite integer arguments to compare them.
     * @return int return the max integer value among the input.
     */
    public static int max(int... args)
    {
        int maximum = args[0];
        for (int i = 1; i < args.length; i++)
            if (maximum < args[i])
                maximum = args[i];

        return maximum;
    }

    /**
     * This method will find the minimum object within the input arguments. Time Complexity: O(args.length)
     * @param args This method will take in infinite integer arguments to compare them.
     * @return int return the min integer value among the input.
     */
    public static int min(int... args)
    {
        int minimum = args[0];
        for (int i = 1; i < args.length; i++)
            if (minimum > args[i])
                minimum = args[i];

        return minimum;
    }

    /**
     * This method will be used to take log on errors. The error log format will be like 
     * errMsg: exception name: exception message
     * @param errMsg First parameter taken by this method, representing the error message to be recorded.
     * @param errObj Last parameter taken by this method, representing the error class.
     */
    public static void errorLog(String errMsg, Throwable errObj)
    {
    	// store original stderr
    	PrintStream stderr = System.err;  

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // reassign the stream
        System.setErr(new PrintStream(os));  
        
        // in order to .printStackTrace() on os instead of stderr
        errObj.printStackTrace();  

        log.info("{}: {}\n{}", errMsg, errObj.toString(), os.toString());

        // reset as stderr
        System.setErr(stderr);  
    }

    /**
     * This method is used to log all elements.
     * @param msg First parameter taken by this method, which is the message to be logged.
     * @param items Last parameter taken by this method, which is the list of items to be logged.
     */
    public static void arrayLog(String msg, List<String> items){
        StringBuilder logBuilder = new StringBuilder();

        for (int i = 0; i < items.size(); i++){
            logBuilder.append(items.get(i));
            if (i != items.size() - 1)
                logBuilder.append(", ");
        }

        log.info("{}: {}", msg, logBuilder.toString());
    }
}
