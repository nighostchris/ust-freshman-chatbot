package com.cse3111project.bot.spring.category.transport;

import java.sql.SQLException;

import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

// import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

// need web grabber to grab (best) estimated arrival time from KMB official website
public class Bus extends Transport {
    public static final String QUERY_KEYWORD[];

    public static final String ROUTE_91_KEYWORD[]  = { "91", "route 91", "91 route" };
    public static final String ROUTE_91M_KEYWORD[] = { "91M", "route 91M", "91M route" };

    public static final String DIRECTION_KEYWORD[] = { "north gate", "south gate" };
    public static final int NORTH = 0;
    public static final int SOUTH = 1;

    static {
        QUERY_KEYWORD = Utilities.concatArrays(new String[] { "bus", "kmb" }, DIRECTION_KEYWORD,
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

    Bus(int busRoute, int location) {
        userQuery = new BusQuery(busRoute, location);
    }

    // obtain the most accurate arrival time from kmb.hk
    public String getArrivalTimeFromKMB() throws Exception {
        // initialize and start crawling from kmb.hk
        BusDetail kmbDatabase = new BusDetail();

        return kmbDatabase.webCrawling();

        // ArrayList<String> results = new ArrayList<String>();

        // if (userQuery.location == NORTH){
        //     if (userQuery.busRoute == ROUTE_91)
        //         results = kmbDatabase.getForNorthGate(ROUTE_91);
        //     else
        //         results = kmbDatabase.getForNorthGate(ROUTE_91M);
        // }
        // else {
        //     if (userQuery.busRoute == ROUTE_91)
        //         results = kmbDatabase.getForSouthGate(ROUTE_91);
        //     else
        //         results = kmbDatabase.getForSouthGate(ROUTE_91M);
        // }

        // return "The next " + results.size() + " route " + (userQuery.busRoute == ROUTE_91 ? "91" : "91M") + 
        //        " bus arrival time:\n" + results.toString() + '\n';
    }

    // may not be necessary
    // @Override
    // public String getArrivalTimeFromSQL() throws SQLException {
    //     return "*** not implemented yet ***";
    // }

    // @Override
    // public String getArrivalTimeFromStatic() throws StaticDatabaseFileNotFoundException {
    //     return "*** not implemented yet ***";
    // }
}
