package com.cse3111project.bot.spring;

import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

////
// The following code contains a bug in it. You need to fix it in Lab 2 in 
// order to make it work.
// After you fix the code, the bot should be able to response based on 
// database.txt located in 
// sample-spring-boot-kitchensink/resources/static/database.txt.
//
// This file contains a few lines with the format <input>:<output>.
// The program below wish to perform an exact word match the input text
// against the <input> against each line. The bot should replys 
// "XXX says <output>"
// For instance, if the client sends "abc", the bot should reply 
// "kevinw says def" 
// If you registered your ITSC login as kevinw.
////

@Slf4j
public class DatabaseEngine {
    // load database from local file
	private final String FILENAME = "/static/database.txt";

    // exact + partial match
	String search(String text) throws Exception {
		String result = null;  // final resulting output by computer
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(this.getClass().getResourceAsStream(FILENAME));
			br = new BufferedReader(isr);
			String sCurrentLine;

            // *** always return the first matching result ***
			while (result == null && (sCurrentLine = br.readLine()) != null) {
                // only interested in key
				String data[] = sCurrentLine.split(":");  // [0]: key, [1]: value
                // search by key
				if (text.toLowerCase().equals(data[0].toLowerCase()))  // exact match
					result = data[1];
                else if (text.toLowerCase().contains(data[0].toLowerCase()))  // key embedded in text and
                    result = data[1];                                         // having exact sequence as key
			}
		}
        catch (IOException e) {
			log.info("IOException while reading file: {}", e.toString());
		} 
        finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} 
            catch (IOException ex) {
				log.info("IOException while closing file: {}", ex.toString());
			}
		}

		if (result != null) return result;
		throw new Exception("NOT FOUND");
    }
}
