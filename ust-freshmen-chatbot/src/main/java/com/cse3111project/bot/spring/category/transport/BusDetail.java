package com.cse3111project.bot.spring.category.transport;

import java.net.URI;

import java.util.Scanner;
import java.util.ArrayList;

import org.json.*;

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

    // private ArrayList<String> ROUTE91MSouthGateArrivalTime = new ArrayList<>();
    // private ArrayList<String> ROUTE91SouthGateArrivalTime = new ArrayList<>();
    // private ArrayList<String> ROUTE91MNorthGateArrivalTime = new ArrayList<>();
    // private ArrayList<String> ROUTE91NorthGateArrivalTime = new ArrayList<>();
	
    // only visible to Bus (Transport package)
	// BusDetail() throws Exception
	// {
    //     webCrawling();
	// }
	
    // crawling data from KMB database
	// private void webCrawling() throws Exception
	// {
    //     // 91M from UST to Choi Hung
    //     URI uToCHM = new URI(ROUTE91MSOUTHGATE);
    //     // 91 from UST to Choi Hung
    //     URI uToCHNotM = new URI(ROUTE91SOUTHGATE);
    //     // 91M from UST to Po Lam
    //     URI uToPoLam = new URI(ROUTE91MNORTHGATE);
    //     // 91 from UST to Clear Water Bay
    //     URI uToCWB = new URI(ROUTE91NORTHGATE);
    //     // call update function
    //     // ROUTE91MSouthGateArrivalTime = updateETA(uToCHM);
    //     // ROUTE91SouthGateArrivalTime = updateETA(uToCHNotM);
    //     // ROUTE91MNorthGateArrivalTime = updateETA(uToPoLam);
    //     // ROUTE91NorthGateArrivalTime = updateETA(uToCWB);
	// }
	
    // obtain and extract ETA from JSON object
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
	
    // ** may be changed as static later **
	// get eta for south gate stop
	// int 0 for getting 91M and int 1 for getting 91
	// return ArrayList of 3 strings about eta of next 3 bus
	static ArrayList<String> getForSouthGate(int mornotm) throws Exception
	{
        return updateETA(new URI(mornotm == 0 ? ROUTE91MSOUTHGATE : ROUTE91SOUTHGATE));
	}
	
    // ** may be changed as static later **
	// get eta for north gate stop
	// int 0 for getting 91M and int 1 for getting 91
	// return ArrayList of 3 strings about eta of next 3 bus
	static ArrayList<String> getForNorthGate(int mornotm) throws Exception
	{
        return updateETA(new URI(mornotm == 0 ? ROUTE91MNORTHGATE : ROUTE91NORTHGATE));
	}
}
