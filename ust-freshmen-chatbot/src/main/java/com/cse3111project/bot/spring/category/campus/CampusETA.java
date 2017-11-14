package com.cse3111project.bot.spring.category.campus;

import java.net.URL;
import java.net.URLEncoder;

import java.net.MalformedURLException;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

public class CampusETA extends Campus
{
	private String startPoint;
	private String endPoint;
	private double eta;
	private CampusMapping timeMatrix;
	
	private static final double MINUTE_PER_PIXEL = 0.000546;
	
	private static String parentURL[] = { "http://pathadvisor.ust.hk/phplib/keyword_suggestion.php?keyword=", "&floor=Overall" };
	
	public static final String CAMPUS_DIRECTION_KEYWORD[] = { "from", "to" };
	
	// public CampusETA ()
	// {
	// 	startPoint = "";
	// 	endPoint = "";
	// 	eta = 0;
	// 	timeMatrix = new CampusMapping();
	// }
	
	CampusETA(String startPoint, String endPoint) throws FileNotFoundException
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		eta = 0;
		timeMatrix = new CampusMapping();
	}

    // try to detect location name, e.g. LTA, from transformed user query
    // @param userQuery: omitted symbols (!@#$%...) + toLowerCase()
    public static void detectLocationName(String userQuery, ArrayList<String> matchedResults){
        StringBuilder fromLocation = new StringBuilder();
        StringBuilder toLocation = new StringBuilder();

        int i = userQuery.indexOf(CAMPUS_DIRECTION_KEYWORD[0], 0);
        if (i == -1) return;  // no "from"
        int j = userQuery.indexOf(CAMPUS_DIRECTION_KEYWORD[1], i);  // should be after "from"
        if (j == -1) return;  // no "to"

        // i = userQuery.indexOf(' ', i);  // after the end of "from"
        // append from "from ..." until " to"
        for (int k = i; k < j - 1; k++)
            fromLocation.append(userQuery.charAt(k));

        // j = userQuery.indexOf(' ', j);  // after the end of "to"
        // if (j == -1) return;
        toLocation.append(userQuery.substring(j));  // "to ..."

        // remove all '.' (fullstop) in toLocation if exist
        // it should not exist while querying location
        // NOTE that it is not trimmed in SearchEngine.parse() 
        // since it is part of keyword for Staff query: Prof.
        int k = 0;
        while (true) {
            k = toLocation.indexOf(".", k);
            if (k == -1) break;
            toLocation = toLocation.deleteCharAt(k);
        }

        // eliminate the possiblities of  "from to", "from LTA to", "from to LTA", ...
        if (fromLocation.length() == 0 || toLocation.length() == 0)
            return;

        // fromLocation: "from ..."
        // toLocation: "to ..."
        matchedResults.add(fromLocation.toString());
        matchedResults.add(toLocation.toString());

        Utilities.arrayLog("matchedResults after CampusETA.detectLocationName()", matchedResults);
    }
	
	// use this function to check if the room inputted by user is valid before calculating the eta
	// should done in category class
	public static boolean locationValid(String startPoint, String endPoint) 
            throws MalformedURLException, IOException
	{
        URL startDetail = null; URL endDetail = null;
        Scanner start = null; Scanner end = null;
        try {
            // use URLEncoder to ensure the URL is valid (has proper HTML encoding)
            // so that the connection could be opened
            startDetail = new URL(parentURL[0] + URLEncoder.encode(startPoint, "UTF-8") + parentURL[1]);
            endDetail = new URL(parentURL[0] + URLEncoder.encode(endPoint, "UTF-8") + parentURL[1]);
            start = new Scanner(startDetail.openStream());
            end = new Scanner(endDetail.openStream());

            // read one line should be fine
            return start.hasNext() && end.hasNext();
        }
        catch (MalformedURLException e) {  // should not occur in Release
            Utilities.errorLog("Bad URL", e);
            throw new MalformedURLException("Unexpected Error occurred. Sorry");
        }
        catch (IOException e)
        {
            Utilities.errorLog("I/O error occurred while querying location on pathadvisor.ust.hk", e);
            throw new IOException("Unexpected Error occurred while interpreting your location. Sorry");
        }
        finally {
            if (start != null)
                start.close();
            if (end != null)
                end.close();
        }

		// if (start.hasNext())
		// {
		// 	String line = start.nextLine();
		// 	countStart += line.length();
		// }
		// 
		// if (end.hasNext())
		// {
		// 	String line = end.nextLine();
		// 	countStart += line.length();
		// }
		
		// if (countStart == 0 || countEnd == 0)
		// 	return false;
		// return true;
	}
	
	public String getStartPoint() { return startPoint; }
	
	public String getEndPoint() { return endPoint; }
	
	public double getETA() { return eta; }
	
	private void calculateETA() throws MalformedURLException, IOException
	{
        URL startDetail = null; URL endDetail = null;
        Scanner start = null; Scanner end = null;

        try {
            // use URLEncoder to make sure the HTML encoding is correct
            startDetail = new URL(parentURL[0] + URLEncoder.encode(startPoint, "UTF-8") + parentURL[1]);
            endDetail = new URL(parentURL[0] + URLEncoder.encode(endPoint, "UTF-8") + parentURL[1]);
            start = new Scanner(startDetail.openStream());
            end = new Scanner(endDetail.openStream());

            String s[] = start.nextLine().split("[,;]");
            String e[] = end.nextLine().split("[,;]");
			
            double firstDraft = distanceToMinute(Integer.parseInt(s[2]), Integer.parseInt(s[3]), 
                                                 Integer.parseInt(e[2]), Integer.parseInt(e[3]));
            this.eta = adjustDraft(s[4], e[4], firstDraft);
        }
        catch (MalformedURLException e) {  // should not occur in Release
            Utilities.errorLog("Bad URL", e);
            throw new MalformedURLException("Unexpected Error occurred. Sorry");
        }
        catch (IOException e)
        {
            Utilities.errorLog("I/O error occurred while querying location on pathadvisor.ust.hk", e);
            throw new IOException("Unexpected Error occurred while interpreting your location. Sorry");
        }
        finally {
            if (start != null)
                start.close();
            if (end != null)
                end.close();
        }
	}
	
	private double distanceToMinute(int x1, int y1, int x2, int y2)
	{
		double distance = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
		return distance * MINUTE_PER_PIXEL;
	}
	
	private double adjustDraft(String startFloor, String endFloor, double firstDraft)
	{
		int start = timeMatrix.locationOnMatrix(startFloor);
		int end = timeMatrix.locationOnMatrix(endFloor);
		int adjust = timeMatrix.getTimeFromMatrix(start, end);
		return firstDraft + (double)adjust / 60;
	}
	
	private String numberToTimeString(double eta)
	{
		String result = "";
		Double d = new Double(eta);
		int min = d.intValue();
		result = result + Integer.toString(min) + " mins and ";
		
		eta -= min;
		d = new Double(eta / 1 * 60);
		min = d.intValue();
		result = result + Integer.toString(min) + " seconds";
		
		return result;
	}
	
	public String getCampusETA() throws MalformedURLException, IOException
	{
		calculateETA();
		return this.toString();
	}
	
	@Override
	public String toString()
	{
		return "It takes " + numberToTimeString(eta) + " to go from " + startPoint + " to " + endPoint;
	}
	
	// test locally
	/*public static void main(String[] main)
	{
		CampusETA test = new CampusETA("4016D", "1001C");	// nab to cyt
		CampusETA test2 = new CampusETA("709A", "6010");	// cyt to cyt
		CampusETA test3 = new CampusETA("LTB", "3001");		// ab to ias
		CampusETA test4 = new CampusETA("LTB", "G021");		// ab to nab
		CampusETA test5 = new CampusETA("3014D", "7011");	// cyt to nab
		CampusETA test6 = new CampusETA("2011", "7011");	// ias to nab
		try 
		{
			test.calculateETA();
			System.out.println(test.toString());
			test2.calculateETA();
			System.out.println(test2.toString());
			test3.calculateETA();
			System.out.println(test3.toString());
			test4.calculateETA();
			System.out.println(test4.toString());
			test5.calculateETA();
			System.out.println(test5.toString());
			test6.calculateETA();
			System.out.println(test6.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
