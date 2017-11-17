package com.cse3111project.bot.spring.category.transport;

import java.sql.SQLException;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

/**
 * The Bus class inherits from the Transport category and handle all user query about
 * estimaed arrival time of KMB bus in the campus.
 * @version 1.0
 */
public class Bus extends Transport 
{
    public static final String QUERY_KEYWORD[];

    public static final String ROUTE_91_KEYWORD[]  = { "91", "91 route", "route 91", 
                                                       "91 bus", "bus route 91", "bus 91 route" };
    public static final String ROUTE_91M_KEYWORD[] = { "91M", "91M route", "route 91M",
                                                       "91M bus", "bus route 91M", "bus 91M route" };

    public static final String DIRECTION_KEYWORD[] = { "north gate", "south gate" };
    public static final int NORTH = 0;
    public static final int SOUTH = 1;

    static 
    {
        QUERY_KEYWORD = Utilities.concatArrays(DIRECTION_KEYWORD,
                                               ROUTE_91_KEYWORD, ROUTE_91M_KEYWORD);
    }

    // constants (enums) defined based on crawler's argument
    public static final int ROUTE_91M = 0;
    public static final int ROUTE_91  = 1;

    // group the user query info together
    class BusQuery {
        // 91M => 0; 91 => 1
        private int busRoute;
        // north gate => 0; south gate => 1
        private int location;

        // only visible to this class
        private BusQuery(int busRoute, int location){
            this.busRoute = busRoute; this.location = location;
        }
    }

    private BusQuery userQuery = null;
    private ArrayList<String> results = new ArrayList<>();

    /**
     * This is the constructor of Bus class, which will initalize an object of inner Class
     * BusQuery, storing further details of the bus query.
     * @param busRoute First parameter taken in the constructor, indicating the code of Bus
     * 				   Route to be stored (91 or 91M).
     * @param location Second parameter taken in the constructor, indicating the location of 
     * 				   Bus Stop in UST Campus (South or North gate bus stop).
     */
    Bus(int busRoute, int location) 
    {
        userQuery = new BusQuery(busRoute, location);
    }

    /**
     * This method requires no parameter, which will obtain the estimated arrival time of
     * bus from official KMB Company.
     * @return String This method will return a string which contains all the available
     * 				  estimated arrival time of bus from the KMB database
     * @throws Exception This method will throw Exception when there is a failure connecting
     * 		   to the KMB database or some URL malforming exception.
     */
    public String getArrivalTimeFromKMB() throws Exception {
        if (userQuery.location == NORTH){
            if (userQuery.busRoute == ROUTE_91)
                results = BusDetail.getForNorthGate(ROUTE_91);
            else
                results = BusDetail.getForNorthGate(ROUTE_91M);
        }
        else {
            if (userQuery.busRoute == ROUTE_91)
                results = BusDetail.getForSouthGate(ROUTE_91);
            else
                results = BusDetail.getForSouthGate(ROUTE_91M);
        }

        return super.replyResults();
    }
    
    /**
     * This method will further process the data retrieved from KMB database and make it to
     * be more clear to the user about the bus arrival time.
     * @return String This method will return a String representation of Bus class with all 
     * 		   the details embedded.
     */
    @Override
    public String toString(){
        if (results.isEmpty())
            return "Currently there is no available arrival time from KMB database. Sorry";

        if (results.contains("撠曄頠歇���"))
            return "You have missed the last " + (userQuery.busRoute == ROUTE_91 ? "91" : "91M") + " bus." + 
                   " Try to take minibus instead.";

        return "The next " + results.size() + " route " + (userQuery.busRoute == ROUTE_91 ? "91" : "91M") + 
               " bus arrival time:\n" + results.toString();
    }
}
