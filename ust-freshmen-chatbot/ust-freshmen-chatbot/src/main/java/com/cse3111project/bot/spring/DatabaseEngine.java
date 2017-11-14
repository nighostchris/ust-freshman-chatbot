// package com.cse3111project.bot.spring;
// 
// import java.util.Arrays;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.io.IOException;
// import lombok.extern.slf4j.Slf4j;  // logging

// this class is deprecated
// @Slf4j
// public class DatabaseEngine {
//     // load database from local file
// 	private final String FILENAME = "/static/database.txt";
// 
//     // *** may be deprecated ***
//     // exact + partial match
// 	String search(String userQuery) throws Exception {
// 		String result = null;  // final resulting output by computer
// 		BufferedReader br = null;
// 		InputStreamReader isr = null;
// 		try {
// 			isr = new InputStreamReader(this.getClass().getResourceAsStream(FILENAME));
// 			br = new BufferedReader(isr);
// 			String sCurrentLine;
// 
//             // *** always return the first matching result ***
// 			while (result == null && (sCurrentLine = br.readLine()) != null) {
//                 // only interested in key
// 				String data[] = sCurrentLine.split(":");  // [0]: key, [1]: value
//                 // search by key
// 				if (userQuery.toLowerCase().equals(data[0].toLowerCase()))  // exact match
// 					result = data[1];
//                 else if (userQuery.toLowerCase().contains(data[0].toLowerCase()))  // key embedded in text and
//                     result = data[1];                                         // having exact sequence as key
// 			}
// 		}
//         catch (IOException e) {
// 			log.info("IOException while reading file: {}", e.toString());
// 		} 
//         finally {
// 			try {
// 				if (br != null)
// 					br.close();
// 				if (isr != null)
// 					isr.close();
// 			} 
//             catch (IOException ex) {
// 				log.info("IOException while closing file: {}", ex.toString());
// 			}
// 		}
// 
// 		if (result != null) return result;
// 		throw new Exception("NOT FOUND");
//     }
// }
