package com.cse3111project.bot.spring.category.transport;

import java.sql.PreparedStatement;  // use PreparedStatement to avoid SQL injection
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.SQLException;

import java.io.InputStream;

import java.util.Calendar;
import java.util.Scanner;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

import lombok.extern.slf4j.Slf4j;  // logging

@Slf4j
public class Minibus extends Transport {
    private static final String SQL_TABLE = "minibus11record";
    // use only when the SQL database is failed to load in order not to break the program
    private static final String STATIC_DATABASE = "/static/transport/minibusDatabase.txt";

    public static final String QUERY_KEYWORD[] = { "minibus", "minibus 11", "11 minibus" };

    // attempt to estimate the arrival time of minibus based on self-collected data from SQL database
    // ** might be too slow **
    @Override
    public String getArrivalTimeFromSQL() throws SQLException {
        currentTime = Calendar.getInstance();  // get current time
        // not enough data => use currentHr to approximate first
        int currentHr = currentTime.get(Calendar.HOUR_OF_DAY);  // get current hr in 24-hr format
        // int currentMin = currentTime.get(MINUTE);

        // accumluate minWaitingTime, maxWaitingTime from database to compute the average
        int totalMinWaitingTime = 0;
        int totalMaxWaitingTime = 0;
        int numOfData = 0;
        // enlarge the hour range if timeslot not found
        for (int hrRange = 1; numOfData == 0 && hrRange <= 24; hrRange++){
            // [0..currentHr]
            int pastHr = (currentHr - hrRange + 1 > 0 ? currentHr - hrRange + 1 : 0);
            // [currenHr + 1..24]
            int nextHr = (currentHr + hrRange < 24 ? currentHr + hrRange : 24);
            String pastHrString = (pastHr < 10 ? "0" + pastHr : new Integer(pastHr).toString());
            String nextHrString = (nextHr < 10 ? "0" + nextHr : new Integer(nextHr).toString());

            PreparedStatement SQLQuery = null;
            ResultSet rs = null;
            try {
                // prepare a SQL query
                // String SQLStatement = "SELECT minWaitingTime, maxWaitingTime FROM " + SQL_TABLE +
                //                       " WHERE aboardTime BETWEEN ? AND ?"; 

                // use StringBuilder rather than operator+() (string concatenation operator)
                // in for loop for PERFORMANCE
                String SQLStatement = new StringBuilder("SELECT minWaitingTime, maxWaitingTime ")
                                          .append("FROM ").append(SQL_TABLE)
                                          .append(" WHERE aboardTime ")
                                          .append("BETWEEN ? AND ?").toString();

                SQLQuery = SQLDatabase.prepare(SQLStatement);

                // public static Time java.sql.Time.valueOf(String timeFormat);
                // where timeFormat: hh:mm:ss
                // log.info("pastHrString: {}", pastHrString);
                // log.info("nextHrString: {}", nextHrString);

                Time pastHrTimeFormat = Time.valueOf(pastHrString + ":00:00");
                Time nextHrTimeFormat = null;
                // *** NOTE that if nextHr == 24 => JDBC would convert time format as 00:00:00
                // since it uses  % 24  to try to fix programmers' time REGARDLESS OF invalid
                if (nextHrString.equals("24"))
                    nextHrTimeFormat = Time.valueOf("23:59:59");
                else
                    nextHrTimeFormat = Time.valueOf(nextHrString + ":00:00");

                log.info("pastHrTimeFormat: {}", pastHrTimeFormat);
                log.info("nextHrTimeFormat: {}", nextHrTimeFormat);

                // convert to SQL TIME format
                SQLQuery.setTime(1, pastHrTimeFormat);
                SQLQuery.setTime(2, nextHrTimeFormat);

                rs = SQLQuery.executeQuery();  // NEVER null

                // column format:
                // travelDate isRushHour ChoiHung2UST minWaitingTime maxWaitingTime aboardTime arrivalTime
                while (rs.next()){
                    totalMinWaitingTime += rs.getInt(1);
                    totalMaxWaitingTime += rs.getInt(2);
                    numOfData++;
                }
            }
            // if exception occurs => must execute .close() before exiting this method
            finally {  // after query, .close() resources
                try {
                    if (rs != null)  // safe .close()
                        rs.close();
                    if (SQLQuery != null)
                        SQLQuery.close();
                }
                catch (SQLException e) {  // mostly not happen since not using threading
                    Utilities.errorLog("Unable to close query statement object", e);
                }
            }
        }

        int avgMinWaitingTime = new Long(Math.round((double) totalMinWaitingTime / numOfData)).intValue();
        int avgMaxWaitingTime = new Long(Math.round((double) totalMaxWaitingTime / numOfData)).intValue();

        return "Estimated Arrival Time: " + avgMinWaitingTime + '-' + avgMaxWaitingTime + " min";
    }

    // if unfortunately fail to connect SQL database, load the static file to estimate arrival time
    // *** LAST RESORT ***
    @Override
    public String getArrivalTimeFromStatic() throws StaticDatabaseFileNotFoundException {
        currentTime = Calendar.getInstance();  // get current time
        int currentHr = currentTime.get(Calendar.HOUR_OF_DAY);  // get current hr in 24-hr format

        // initialize array of arrivalTime to compute the avgMinWaitingTime and avgMaxWaitingTime
        ArrayList<ArrivalTime> arrivalTime = new ArrayList<>();
        int minAboardHrDiff = 24;  // attempt to find the minimum aboard hr diff
                                   // initialize to the largest diff |aboardHr - currentHr|
                                   // so that easier to find min

        // read static database
        Scanner staticDatabaseReader = null;
        try {
            InputStream is = this.getClass().getResourceAsStream(STATIC_DATABASE);
            if (is == null)  // static database file not found
                throw new StaticDatabaseFileNotFoundException(STATIC_DATABASE + " file not found");
            // load static database file
            staticDatabaseReader = new Scanner(is);

            // column format:
            // travelDate isRushHour ChoiHung2UST minWaitingTime maxWaitingTime aboardTime arrivalTime
            while (staticDatabaseReader.hasNextLine()){
                String line = staticDatabaseReader.nextLine();
                // start with # is considered as comment, see /static/transport/minibusDatabase.txt
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                String parts[] = line.split(",");
                int aboardHrDiff = Math.abs(new Integer(parts[5].split(":")[0]) - currentHr);
                arrivalTime.add(new ArrivalTime(new Integer(parts[3]), new Integer(parts[4]), 
                                                aboardHrDiff));
                if (aboardHrDiff < minAboardHrDiff)  // find minimum aboard hour difference
                    minAboardHrDiff = aboardHrDiff;
            }
        }
        finally {
            if (staticDatabaseReader != null)  // safe .close() resources
                staticDatabaseReader.close();
        }

        log.info("minAboardHrDiff: {}", minAboardHrDiff);  // for testing

        // accumulate waiting time to compute average
        int totalMinWaitingTime = 0;
        int totalMaxWaitingTime = 0;
        int numOfData = 0;

        for (ArrivalTime arrival : arrivalTime){
            if (arrival.aboardHrDiff == minAboardHrDiff){
                totalMinWaitingTime += arrival.minWaitingTime;
                totalMaxWaitingTime += arrival.maxWaitingTime;
                numOfData++;
            }
        }

        int avgMinWaitingTime = new Long(Math.round((double) totalMinWaitingTime / numOfData)).intValue();
        int avgMaxWaitingTime = new Long(Math.round((double) totalMaxWaitingTime / numOfData)).intValue();

        return "Estimated Arrival Time: " + avgMinWaitingTime + '-' + avgMaxWaitingTime + " min";
    }

    class ArrivalTime {
        private int minWaitingTime;
        private int maxWaitingTime;
        private int aboardHrDiff;  // difference between currentHr and aboardHr |currentHr - aboardHr|
                                   // currently only consider aboard hour because of the lack of data

        // only be used in Minibus class
        private ArrivalTime(int minWaitingTime, int maxWaitingTime, int aboardHrDiff){
            this.minWaitingTime = minWaitingTime;
            this.maxWaitingTime = maxWaitingTime;
            this.aboardHrDiff = aboardHrDiff;
        }
    }
}
