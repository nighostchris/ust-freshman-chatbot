package com.cse3111project.bot.spring.utility;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;  // logging

// offer general utilities for classes
@Slf4j
public final class Utilities {
    // make sure no one could instantiate Utilities object since all methods here would be static
    private Utilities() { }

    // concatenate two arrays, might be expanded as
    // - variadic arguments  -- done
    // - generics
    // if needed
    // Time Complexity: O(sum of arrays length)
    public static String[] concatArrays(String[]... arrays){
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

    // log all elements
    public static void arrayLog(String msg, ArrayList<String> items){
        StringBuilder logBuilder = new StringBuilder();

        for (int i = 0; i < items.size(); i++){
            logBuilder.append(items.get(i));
            if (i != items.size() - 1)
                logBuilder.append(", ");
        }

        log.info("{}: {}", msg, logBuilder.toString());
    }
}
