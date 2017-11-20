package com.cse3111project.bot.spring.category.social;

import com.cse3111project.bot.spring.model.engine.marker.SQLAccessible;
import com.cse3111project.bot.spring.model.engine.SQLDatabaseEngine;
import com.cse3111project.bot.spring.model.engine.marker.StaticAccessible;
import com.cse3111project.bot.spring.model.engine.StaticDatabaseEngine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.net.URISyntaxException;

import java.util.Scanner;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;
import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * The Societies class inherits from Social category and handles all the user query about
 * societies and clubs available in UST.
 * @version 1.0
 */
@Slf4j
public class Societies extends Social implements SQLAccessible, StaticAccessible 
{
    public static final String QUERY_KEYWORD[];

    public static final String SOCIETY_KEYWORD[] = { "Chinese Folk Art Society",
                                                     "Chinese Orchestra",
                                                     "Comics and Animation Society",
                                                     "Culinary Art and Culture Society",
                                                     "Drama Society",
                                                     "Film Society",
                                                     "International Cuisine Society",
                                                     "Korean Students' Association",
                                                     "Magic Club",
                                                     "South Asian Students' Society",
                                                     "Southeast Asian Students' Society",
                                                     "Taiwanese Students'Association",
                                                     "The Band Society",
                                                     "The Photographic Society",
                                                     "The University Choir",
                                                     "University Philharmonic Orchestra",
                                                     "Independent Clubs Association",
                                                     "AIESEC-LC-HKUST",
                                                     "Campus Crusade for Christ",
                                                     "China Entrepreneur Network",
                                                     "China Study Society",
                                                     "Chinese Culture Society",
                                                     "Christian Choir",
                                                     "Christian Fellowship",
                                                     "Contract Bridge Club",
                                                     "Debating Society",
                                                     "Games Society",
                                                     "Golden Z Club",
                                                     "Greater China Vision",
                                                     "Hong Kong Career Match Association",
                                                     "Model United Nations Club",
                                                     "Nature Club",
                                                     "People's Campus Radio",
                                                     "Progressust",
                                                     "Rotaract Club of HKUST",
                                                     "Student Air Cadet Society",
                                                     "Student Astronomy Club",
                                                     "Student Social Service Society",
                                                     "The Catholic Society",
                                                     "The Hong Kong Award for Young People User Unit",
                                                     "The Linguistics Society",
                                                     "University YMCA - HKUST",
                                                     "Yo-hoo Club",
                                                     "Sports Association",
                                                     "Archery Club",
                                                     "Cricket Club",
                                                     "Dance Society",
                                                     "Distance Runner's Club",
                                                     "Dodgeball Club",
                                                     "Dragon Boat Club",
                                                     "Fencing Club",
                                                     "Handball Club",
                                                     "Hockey Club",
                                                     "Judo Club",
                                                     "Kendo Club",
                                                     "Korfball Club",
                                                     "Rope Skipping Club",
                                                     "Rowing Club",
                                                     "Rugby Club",
                                                     "Shaolin Martial Arts Society",
                                                     "Social Dance Society",
                                                     "Softball Club",
                                                     "Sport Climbing Students' Society",
                                                     "Squash Club",
                                                     "Student Basketball Club",
                                                     "Students' Badminton Club",
                                                     "Students' Football Club",
                                                     "Students' Karate Club",
                                                     "Students' Swimming Club",
                                                     "Table Tennis Club",
                                                     "Tae Kwon Do Club",
                                                     "Tai Chi Society",
                                                     "Tchoukball Club",
                                                     "The Netball Club",
                                                     "Track and Field Club",
                                                     "Volleyball Club",
                                                     "Wing Chun Martial Arts Society",
                                                     "The Undergraduate House One Students' Association",
                                                     "VERTEX, House II Students' Association",
                                                     "Glacier, HOUSE III Students' Association",
                                                     "Vista, House IV Students' Association",
                                                     "Endeavour, House V Students' Association",
                                                     "The Business Students' Union",
                                                     "The Engineering Students' Union",
                                                     "The Humanities and Social Science Students' Union",
                                                     "Science Students' Union",
                                                     "Art Club",
                                                     "Chemistry Students' Society",
                                                     "Mathematics Students' Society",
                                                     "International Research Enrichment Students' Society",
                                                     "The Biology Students' Society",
                                                     "The Physics Students' Society",
                                                     "Environment Students' Society",
                                                     "Risk Management and Business Intelligence Students' Association",
                                                     "Artiste and Show Production Association",
                                                     "Current Affairs Research Enlightenment",
                                                     "Gym and Fitness Society",
                                                     "The Student Tennis Club",
                                                     "Interdisciplinary Programs Students' Union,HKUSTSU",
                                                     "Accounting Students' Society",
                                                     "Information Systems, Business Statistics and Operations Management Students' Society",
                                                     "Management Students' Association",
                                                     "The Economics Students' Society",
                                                     "The Finance Students' Society",
                                                     "The Marketing Students' Society",
                                                     "Computer Engineering Students' Society",
                                                     "Industrial Engineering and Logistics Management Students' Society",
                                                     "Mechanical Engineering Students' Association",
                                                     "The Chemical and Biomolecular Engineering Students' Society",
                                                     "The Civil and Environmental Engineering Students' Society",
                                                     "The Computer Science and Engineering Students' Society",
                                                     "The Electronic and Computer Engineering Students' Society",
                                                     "Biochemistry Students' Society"
                                                 };

    static 
    {
        QUERY_KEYWORD = Utilities.concatArrays(new String[] { "HKUST society", "HKUST soc", "HKUST societies",
                                                              "UST society", "UST soc", "UST societies" },
                                               SOCIETY_KEYWORD);
    }

    public static final String SOCIETIES_LINK = "https://hkustsucouncil.wordpress.com/standing-committees/the-affiliated-societies-committee/list-of-affiliated-societies/";

    // format:
    // id name website_link
    private static final String SQL_TABLE = "hkust_societies";
    private static final String STATIC_TABLE = "/static/social/societiesDatabase.txt";

    // user may query more than 1 societies
    private ArrayList<String> userQuery;
    private ArrayList<SocietyInfo> results;

    /**
     * Constructor of Societies class, which will store the processed user-query as instance variable.
     * @param userQuery The only parameter taken by constructor, which is a list of keywords extracted from user-query.
     */
    Societies(final ArrayList<String> userQuery)
    {
        this.userQuery = userQuery;  // MUST NOT BE EMPTY
    }

    /**
     * The SocietyInfo class is the inner class of Societies, which is used to wrap society info.
     */
    class SocietyInfo
    {
        private String name;
        private String link;

        /**
         * Constructor of SocietyInfo.
         * @param name Name of society / club.
         * @param link Website of society / club if available.
         */
        private SocietyInfo(String name, String link)
        {
            this.name = name; this.link = link;
        }

        /**
         * This method converts all instance variable of a SocietyInfo object as a single String.
         */
        @Override
        public String toString(){
            return name + ": " + link + '\n';
        }
    }

    /**
     * This method will connect to the SQL database and retrieve data on details of societies and clubs.
     * @return String
     * @throws NotSQLAccessibleError Throws error when the class is not SQLAccessible.
     * @throws URISyntaxException
     * @throws SQLException
     */
    @Override
    public synchronized String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException {
        try (SQLDatabaseEngine database = new SQLDatabaseEngine(this, SQL_TABLE)) {
            results = new ArrayList<>();

            // user query MUST NOT be empty
            StringBuilder SQLStatement = new StringBuilder("SELECT name, website_link FROM ")
                                             .append(SQL_TABLE)
                                             .append(" WHERE name ILIKE concat(\'%\', ?, \'%\')");

            for (int i = 1; i < userQuery.size(); i++)
                SQLStatement.append(" OR name ILIKE concat(\'%\', ?, \'%\')");

            log.info("SQLStatement: {}", SQLStatement.toString());

            PreparedStatement SQLQuery = database.prepare(SQLStatement.toString());
            for (int i = 0; i < userQuery.size(); i++)
                SQLQuery.setString(i + 1, userQuery.get(i));

            ResultSet reader = database.executeQuery();
            while (reader.next())
                results.add(new SocietyInfo(reader.getString(1), reader.getString(2)));

            return super.replyResults(results);
        }
    }

    /**
     * This method will connect to the static database and retrieve data on details of societies and clubs.
     * It is used when the SQL database has run into some trouble of connection.
     * @return String
     * @throws NotStaticAccessibleError Throws error when the class is not SQLAccessible.
     * @throws StaticDatabaseFileNotFoundException Throws error when static database can't be found / connected.
     */
    @Override
    public synchronized String getDataFromStatic() throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException {
        try (StaticDatabaseEngine database = new StaticDatabaseEngine(this, STATIC_TABLE)) {
            results = new ArrayList<>();

            Scanner reader = database.executeQuery();
            while (reader.hasNextLine()){
                String line = reader.nextLine();
                // starting with # is considered as an comment
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                String societyName = line.split(",")[1];
                if (userQuery.contains(societyName))
                    results.add(new SocietyInfo(societyName, line.split(",")[2]));
            }

            return super.replyResults(results);
        }
    }
}
