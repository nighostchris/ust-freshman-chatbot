package com.cse3111project.bot.spring.category.transport;

import java.net.URI;

import java.util.Scanner;
import java.util.ArrayList;

import org.json.*;

/**
 * The BusDetail Class handles all the actual data retrieval from the KMB database and return the 
 * result. It was used and called in Bus Class when needed.
 * @version 1.0
 */
final class BusDetail
{
    // 91M from UST to Choi Hung
    private static final String ROUTE91MSOUTHGATE = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91M&bound=1&stop=HK02T10000&stop_seq=12&serviceType=1";
    // 91 from UST to Choi Hung
    private static final String ROUTE91SOUTHGATE = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91&bound=1&stop=HK02T10000&stop_seq=15&serviceType=1";
    // 91M from UST to Po Lam
    private static final String ROUTE91MNORTHGATE = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91M&bound=1&stop=HK01T11000&stop_seq=16&serviceType=1";
    // 91 from UST to Clear Water Bay
    private static final String ROUTE91NORTHGATE = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91&bound=1&stop=HK01T10500&stop_seq=15&serviceType=1";
	
    /** 
     * This method will connect to KMB database, retrieve the data in JSON format and
     * perform further process on the data.
     * @param uri This method will take in a URI object which represents the link to corresponding
     * 			  KMB database.
     * @return ArrayList<String> This method will return an ArrayList of String which contains all
     * 							 the available estimated arrival time of bus from the Bus Database.
     * @throws Exception This method will throw Exception when encounter Database-connection or
     * 					 URL malform problem.
     */
	private static ArrayList<String> updateETA(URI uri) throws Exception
	{
        ArrayList<String> arrivalTime = new ArrayList<>();

        // get json from kmb
        Scanner sc = new Scanner(uri.toURL().openStream());
		String json = sc.nextLine();
		
		// get the 3 eta for bus route
		JSONObject obj = new JSONObject(json);
		JSONArray time = obj.getJSONArray("response");
		for (int i = 0; i < time.length(); i++)
		{
            JSONObject eta = time.getJSONObject(i);
            String etaTime = eta.getString("t").substring(0, 5);
            arrivalTime.add(etaTime);
		}

        sc.close();

        return arrivalTime;
	}
	
	/**
	 * This method will return the processed data of estimated arrival time of bus in South
	 * gate bus stop of UST.
	 * @param mornotm Only required parameter of the function, which represents whether the bus
	 * 				  route is 91 or 91M. Integer value of 0 means 91M while 1 means 91.
	 * @return ArrayList<String> This method will return an ArrayList of String which contains
	 * 							 all the ETA of bus from KMB database.
	 * @throws Exception
	 */
	static ArrayList<String> getForSouthGate(int mornotm) throws Exception
	{
        return updateETA(new URI(mornotm == 0 ? ROUTE91MSOUTHGATE : ROUTE91SOUTHGATE));
	}
	
	/**
	 * This method will return the processed data of estimated arrival time of bus in North
	 * gate bus stop of UST.
	 * @param mornotm Only required parameter of the function, which represents whether the bus
	 * 		  route is 91 or 91M. Integer value of 0 means 91M while 1 means 91.
	 * @return ArrayList<String> This method will return an ArrayList of String which contains
	 * 							 all the ETA of bus from KMB database.
	 * @throws Exception
	 */
	static ArrayList<String> getForNorthGate(int mornotm) throws Exception
	{
        return updateETA(new URI(mornotm == 0 ? ROUTE91MNORTHGATE : ROUTE91NORTHGATE));
	}
}
