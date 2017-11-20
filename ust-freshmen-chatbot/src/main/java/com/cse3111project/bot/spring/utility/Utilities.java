package com.cse3111project.bot.spring.utility;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import java.util.List;

import lombok.extern.slf4j.Slf4j;  // logging

// offer general utilities for classes
@Slf4j
public final class Utilities {
    // make sure no one could instantiate Utilities object since all methods here would be static
    private Utilities() { }

    // concatenate two arrays, might be expanded as generics if necessary
    // Time Complexity: O(sum of arrays length)
    public static synchronized String[] concatArrays(String[]... arrays){
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

    // find the maximum between arguments
    // later may be expanded as generics if necessary
    // Time Complexity: O(args.length)
    public static int max(int... args){
        int maximum = args[0];
        for (int i = 1; i < args.length; i++)
            if (maximum < args[i])
                maximum = args[i];

        return maximum;
    }

    // find the minimum between arguments
    // later may be expanded as generics if necessary
    // Time Complexity: O(args.length)
    public static int min(int... args){
        int minimum = args[0];
        for (int i = 1; i < args.length; i++)
            if (minimum > args[i])
                minimum = args[i];

        return minimum;
    }

    // check whether the String array contains result, case-sensitive
    // Time complexity: O(arr.length)
    public static boolean contains(String[] arr, String result){
        for (String element : arr)
            if (element.equals(result))
                return true;

        return false;
    }

    // check whether all args are all equal to each other
    @SafeVarargs
    public static <T> boolean allEquals(T... args){
        // empty set or singleton
        if (args.length == 0 || args.length == 1)
            return true;

        T arg = args[0];
        for (int i = 1; i < args.length; i++)
            if (!arg.equals(args[i]))
                return false;

        return true;
    }

    // error log format:
    // <errMsg>: <exception name>: <exception message>
    // ... <error stack trace> ...
    public static void errorLog(String errMsg, Throwable errObj){
        PrintStream stderr = System.err;  // store original stderr

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setErr(new PrintStream(os));  // reassign the stream
        errObj.printStackTrace();  // in order to .printStackTrace() on os instead of stderr

        log.info("{}: {}\n{}", errMsg, errObj.toString(), os.toString());

        System.setErr(stderr);  // reset as stderr

        // os.close();  // unnecessary by javadoc
    }

    // log all elements
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
