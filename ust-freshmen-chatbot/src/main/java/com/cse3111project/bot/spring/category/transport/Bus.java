package com.cse3111project.bot.spring.category.transport;

import java.sql.SQLException;

// need web grabber to grab (best) estimated arrival time from KMB official website
public class Bus extends Transport {
    public static final String QUERY_KEYWORD[] = { "bus", "kmb", "91", "route 91", "91M", "route 91M",
                                                   "91 route", "91M route" };

    // require SQL Database / Web Grabber from KMB ***

    @Override
    public String getArrivalTimeFromSQL() throws SQLException {
        return "*** not implemented yet ***";
    }

    @Override
    public String getArrivalTimeFromStatic(){
        return "*** not implemented yet ***";
    }
}
