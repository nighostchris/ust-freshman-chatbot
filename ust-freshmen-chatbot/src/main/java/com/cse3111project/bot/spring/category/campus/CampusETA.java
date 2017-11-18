package com.cse3111project.bot.spring.category.campus;

import java.net.URL;
import java.net.URLEncoder;

import java.net.MalformedURLException;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Scanner;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

/**
 * The CampusETA Class inherits from Campus Class, which handle the actual calculation of distance between
 * the 2 locations indicated by user and store the time required as instance variable.
 * @version 1.0
 */
public class CampusETA extends Campus
{
	private String startPoint;
	private String endPoint;
	private double eta;
	private CampusMapping timeMatrix;
	
	private static final double MINUTE_PER_PIXEL = 0.000546;
	
	private static String parentURL[] = { "http://pathadvisor.ust.hk/phplib/keyword_suggestion.php?keyword=", "&floor=Overall" };
	
	public static final String CAMPUS_DIRECTION_KEYWORD[] = { "from", "to" };
	
	/**
	 * This is the constructor of CampusETA Class, which will accept the 2 valid locations by users and
	 * store as instance variable for further calculation later on in this class.
	 * @param startPoint First parameter taken by the constructor, representing the name of the starting 
	 * 					 location of the user.
	 * @param endPoint Second parameter taken by the constructor, representing the name of the destination
	 * 				   that the user wants to go.
	 * @throws FileNotFoundException
	 */
	CampusETA(String startPoint, String endPoint) throws FileNotFoundException
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		eta = 0;
		timeMatrix = new CampusMapping();
	}

    /**
     * This method will try to detect location name within the campus from transformed user query. It is
     * supposed to be called in Category Class.
     * @param userQuery First parameter taken by the method, representing the user query without invalid
     * 				    symbol.
     * @param matchedResults Second parameter taken by the method, representing the keywords detected from the
     * 						 user query.
     */
	public static void detectLocationName(String userQuery, ArrayList<String> matchedResults)
    {
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
	
	/**
	 * This method will check if the location / room inputted by user is valid before calculating the actual
	 * estimated time for the user to walk between. It is supposed to be called in Category class.
	 * @param startPoint First parameter taken by the method, representing the current position of the user.
	 * @param endPoint Second parameter taken by the method, representing the destination of user.
	 * @return Boolean This method will return a boolean value indicating whether the 2 locations inputted by
	 * 				   the user is correct. True when valid locations, false otherwise.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
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
	
	/**
	 * This method is the getter method for instance variable startPoint.
	 * @return String return instance variable startPoint.
	 */
	public String getStartPoint() { return startPoint; }
	
	/**
	 * This method is the getter method for instance variable endPoint.
	 * @return String return instance variable endPoint.
	 */
	public String getEndPoint() { return endPoint; }
	
	/**
	 * This method is the getter method for instance variable eta.
	 * @return double return the instnace variable eta.
	 */
	public double getETA() { return eta; }
	
	/**
	 * This method is going to calculate the estimated time to go from startPoint to endPoint
	 * defined by the user.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
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
	
	/**
	 * This method will calculate the distance between 2 points with the x, y coordinates of them
	 * known to the program, and transform the distance to time required to walk through.
	 * @param x1 First parameter taken by the method, representing the x coordinate of Point 1.
	 * @param y1 Second parameter taken by the method, representing the y coordinate of Point 1.
	 * @param x2 Third parameter taken by the method, representing the x coordinate of Point 2.
	 * @param y2 Last parameter taken by the method, representing the y coordinate of Point 2.
	 * @return double This method will return the time to walk between 2 points in double format.
	 */
	private double distanceToMinute(int x1, int y1, int x2, int y2)
	{
		double distance = Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
		return distance * MINUTE_PER_PIXEL;
	}
	
	/**
	 * This method will further adjust the time estimated in the first draft from calculateETA() method,
	 * which consider also the floor difference and the campus building difference between the 2 points.
	 * @param startFloor First parameter taken by the method, representing the located floor of startPoint.
	 * @param endFloor Second parameter taken by the method, representing the located floor of endPoint.
	 * @param firstDraft Last parameter taken by the method, representing the rough estimation from the 
	 * 					 calculateETA() method.
	 * @return double This method will return the final estimation of the time required in double format.
	 */
	private double adjustDraft(String startFloor, String endFloor, double firstDraft)
	{
		int start = timeMatrix.locationOnMatrix(startFloor);
		int end = timeMatrix.locationOnMatrix(endFloor);
		int adjust = timeMatrix.getTimeFromMatrix(start, end);
		return firstDraft + (double)adjust / 60;
	}
	
	/** 
	 * This method will transform a double type value into a String that represents the time in text format.
	 * @param eta Only parameter taken by the method, representing the time want to transform, e.g. eta.
	 * @return String This method will return a String which represents eta.
	 */
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
	
	/**
	 * This method will calculate the eta between 2 points and return the time result.
	 * @return String the text format of the eta.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getCampusETA() throws MalformedURLException, IOException
	{
		calculateETA();
		return this.toString();
	}
	
	/**
	 * This method will wrap the text format of eta in a nicer way to display to LINE client.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "It takes " + numberToTimeString(eta) + " to go from " + startPoint + " to " + endPoint;
	}
}
