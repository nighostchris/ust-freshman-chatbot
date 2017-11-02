package com.cse3111project.bot.spring.category.transport;

import java.net.URL;

import java.util.Scanner;
import java.util.ArrayList;

import org.json.*;

class BusDetail
{
    // 91M from UST to Choi Hung
    private static final String UST2CHOIHUNG91M = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91M&bound=1&stop=HK02T10000&stop_seq=12&serviceType=1";
    // 91 from UST to Choi Hung
    private static final String UST2CHOIHUNG91 = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91&bound=1&stop=HK02T10000&stop_seq=15&serviceType=1";
    // 91M from UST to Po Lam
    private static final String UST2POLAM91M = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91M&bound=1&stop=HK01T11000&stop_seq=16&serviceType=1";
    // 91 from UST to Clear Water Bay
    private static final String UST2CLEARWATERBAY91 = "http://etav3.kmb.hk/?action=geteta&lang=tc&route=91&bound=1&stop=HK01T10500&stop_seq=15&serviceType=1";

	private ArrayList<String> southGateArrivalTime;
	private ArrayList<String> northGateArrivalTime;
	
    // only visible to Bus (Transport package)
	BusDetail() throws Exception
	{
		southGateArrivalTime = new ArrayList<String>();
		northGateArrivalTime = new ArrayList<String>();
		
        webCrawling();
	}
	
    // crawling data from KMB database
	private void webCrawling() throws Exception
	{
        // 91M from UST to Choi Hung
        URL uToCHM = new URL(UST2CHOIHUNG91M);
        // 91 from UST to Choi Hung
        URL uToCHNotM = new URL(UST2CHOIHUNG91);
        // 91M from UST to Po Lam
        URL uToPoLam = new URL(UST2POLAM91M);
        // 91 from UST to Clear Water Bay
        URL uToCWB = new URL(UST2CLEARWATERBAY91);
        // call update function
        updateETA(uToCHM, 0);
        updateETA(uToCHNotM, 0);
        updateETA(uToPoLam, 1);
        updateETA(uToCWB, 1);
	}
	
    // obtain and extract ETA from JSON object
	private void updateETA(URL url, int gate) throws Exception
	{
		// get json from kmb
		Scanner sc = new Scanner(url.openStream());
		String json = sc.nextLine();
		
		// get the 3 eta for bus route
		JSONObject obj = new JSONObject(json);
		JSONArray time = obj.getJSONArray("response");
		for (int i = 0; i < 3; i++)
		{
			JSONObject eta = time.getJSONObject(i);
			String etaTime = eta.getString("t").substring(0, 5);
			if (gate == 0)
				southGateArrivalTime.add(etaTime);
			else
				northGateArrivalTime.add(etaTime);
		}
		sc.close();
	}
	
	// get eta for south gate stop
	// int 0 for getting 91M and int 1 for getting 91
	// return ArrayList of 3 strings about eta of next 3 bus
	public ArrayList<String> getForSouthGate(int mornotm)
	{
		ArrayList<String> result = new ArrayList<String>();
		if (mornotm == 0)
			for (int i = 0; i < 3; i++)
				result.add(southGateArrivalTime.get(i));
		else
            for (int i = 0; i < 3; i++)
                result.add(southGateArrivalTime.get(i + 3));

        return result;
	}
	
	// get eta for north gate stop
	// int 0 for getting 91M and int 1 for getting 91
	// return ArrayList of 3 strings about eta of next 3 bus
	public ArrayList<String> getForNorthGate(int mornotm)
	{
		ArrayList<String> result = new ArrayList<String>();
		if (mornotm == 0)
			for (int i = 0; i < 3; i++)
				result.add(northGateArrivalTime.get(i));
		else
			for (int i = 0; i < 3; i++)
				result.add(northGateArrivalTime.get(i + 3));

        return result;
	}
}
