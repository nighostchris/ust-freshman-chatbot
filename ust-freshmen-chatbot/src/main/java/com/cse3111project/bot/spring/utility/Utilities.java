package com.cse3111project.bot.spring.utility;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import lombok.extern.slf4j.Slf4j;  // logging

// offer general utilities for classes
@Slf4j
public final class Utilities {
    // make sure no one could instantiate Utilities object since all methods here would be static
    private Utilities() { }

    // concatenate two arrays, might be expanded as
    // - variadic arguments
    // - generics
    // Time Complexity: O(arr1.length + arr2.length)
    public static String[] concatArrays(String arr1[], String arr2[]){
        String newArr[] = new String[arr1.length + arr2.length];

        int i = 0;
        for (; i < arr1.length; i++)
            newArr[i] = arr1[i];
        for (; i < arr1.length + arr2.length; i++)
            newArr[i] = arr2[i - arr1.length];

        return newArr;
    }

    // error log format:
    // <errMsg>: <exception name>: <exception message>
    // ... <error stack trace> ...
    public static void errorLog(String errMsg, Exception errObj){
        PrintStream stderr = System.err;  // store original stderr

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setErr(new PrintStream(os));  // reassign the stream
        errObj.printStackTrace();  // in order to .printStackTrace() on os instead of stderr

        log.info("{}: {}\n{}", errMsg, errObj.toString(), os.toString());

        System.setErr(stderr);  // reset as stderr

        // os.close();  // unnecessary by javadoc
    }
}
