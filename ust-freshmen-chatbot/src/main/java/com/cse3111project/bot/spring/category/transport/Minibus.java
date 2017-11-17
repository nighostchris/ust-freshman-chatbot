package com.cse3111project.bot.spring.category.transport;

import com.cse3111project.bot.spring.model.engine.marker.SQLAccessible;
import com.cse3111project.bot.spring.model.engine.SQLDatabaseEngine;
import com.cse3111project.bot.spring.model.engine.marker.StaticAccessible;
import com.cse3111project.bot.spring.model.engine.StaticDatabaseEngine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.SQLException;

import java.net.URISyntaxException;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Scanner;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;
import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

/**
 * The Minibus class inherits from the Transport category and handle all user query about
 * estimaed arrival time of minibus in the campus.
 * @version 1.0
 */
public class Minibus extends Transport implements SQLAccessible, StaticAccessible 
{
    public static final String QUERY_KEYWORD[] = { "minibus 11", "minibus route 11", 
                                                   "11 minibus", "route 11 minibus" };

    // format:
    // travelDate isRushHour ChoiHung2UST minWaitingTime maxWaitingTime aboardTime arrivalTime
    private static final String SQL_TABLE = "minibus11record";
    private static final String STATIC_TABLE = "/static/transport/minibusDatabase.txt";

    // get current time in Hong Kong
    private Calendar currentTime;

    private int minAboardHrDiff;
    private int avgMinWaitingTime; private int avgMaxWaitingTime;

    private ArrayList<ArrivalTime> arrivalTime;

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

    private void init(){
        currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        // reset as the largest diff |aboardHr - currentHr| so that it would be easier to find min
        this.minAboardHrDiff = 24;
    }

    /**
     * This method requires no parameter, which returns the estimated arrival time of minibus
     * from database.
     * @return String This method will return a string which contains the next available
     * 				  estimated arrival time of minibus.
     * @throws NotSQLAccessibleError
     * @throws URISyntaxException
     * @throws SQLException
     */
    @Override
    public synchronized String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException {
        // *** executing multiple queries on SQL is too slow ***
        // enlarge the hour range if timeslot not found
        // for (int hrRange = 1; numOfData == 0 && hrRange <= 24; hrRange++) { ... }

        try (SQLDatabaseEngine database = new SQLDatabaseEngine(this, SQL_TABLE)) {
            this.init();
            int currentHr = currentTime.get(Calendar.HOUR_OF_DAY);  // get current hr in 24-hr format
            arrivalTime = new ArrayList<>();

            StringBuilder SQLStatement = new StringBuilder("SELECT minWaitingTime, maxWaitingTime, aboardTime FROM ")
                                             .append(SQL_TABLE);

            database.prepare(SQLStatement.toString());

            ResultSet reader = database.executeQuery();
            while (reader.next()){
                int minWaitingTime = reader.getInt(1);
                int maxWaitingTime = reader.getInt(2);
                int aboardHrDiff = Math.abs(new Integer(reader.getTime(3).toString().split(":")[0]) - currentHr);
                arrivalTime.add(new ArrivalTime(minWaitingTime, maxWaitingTime, aboardHrDiff));

                if (aboardHrDiff < this.minAboardHrDiff)  // find minimum aboard hour difference
                    this.minAboardHrDiff = aboardHrDiff;
            }

            this.computeAvgArrivalTime();

            return super.replyResults();
        }
    }

    /**
     * This method requires no parameter, which returns the estimated arrival time of minibus 
     * from static database when we encounter trouble in connecting SQL database.
     * @return String This method will return a string which contains the next available
     * 				  estimated arrival time of minibus.
     * @throws NotStaticAccessibleError
     * @throws StaticDatabaseFileNotFoundException
     */
    @Override
    public synchronized String getDataFromStatic() throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException {
        try (StaticDatabaseEngine database = new StaticDatabaseEngine(this, STATIC_TABLE)) {
            this.init();
            int currentHr = currentTime.get(Calendar.HOUR_OF_DAY);  // get current hr in 24-hr format
            arrivalTime = new ArrayList<>();

            Scanner reader = database.executeQuery();

            while (reader.hasNextLine()){
                String line = reader.nextLine();
                // starting with # is considered as comment
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                String parts[] = line.split(",");
                int aboardHrDiff = Math.abs(new Integer(parts[5].split(":")[0]) - currentHr);
                arrivalTime.add(new ArrivalTime(new Integer(parts[3]), new Integer(parts[4]), 
                                                aboardHrDiff));

                if (aboardHrDiff < this.minAboardHrDiff)  // find minimum aboard hour difference
                    this.minAboardHrDiff = aboardHrDiff;
            }

            this.computeAvgArrivalTime();
            
            return super.replyResults();
        }
    }

    /**
     * This method takes no parameter, which will compute the average arrival time of minibus
     * and store the result to instance variables, not producing any return value.
     */
    private void computeAvgArrivalTime(){
        double totalMinWaitingTime = 0;
        double totalMaxWaitingTime = 0;
        int numOfData = 0;

        for (ArrivalTime arrival : arrivalTime){
            if (arrival.aboardHrDiff == this.minAboardHrDiff){
                totalMinWaitingTime += arrival.minWaitingTime;
                totalMaxWaitingTime += arrival.maxWaitingTime;
                numOfData++;
            }
        }

        this.avgMinWaitingTime = new Long(Math.round(totalMinWaitingTime / numOfData)).intValue();
        this.avgMaxWaitingTime = new Long(Math.round(totalMaxWaitingTime / numOfData)).intValue();
    }

    /**
     * This method will further process the data retrieved from database and make it to
     * be more clear to the user about the minibus arrival time.
     * @return String This method will return a String representation of Minibus class with all 
     * 		   the details embedded.
     */
    @Override
    public String toString(){
        return "Estimated Arrival Time: " + avgMinWaitingTime + '-' + avgMaxWaitingTime + " min";
    }
}
