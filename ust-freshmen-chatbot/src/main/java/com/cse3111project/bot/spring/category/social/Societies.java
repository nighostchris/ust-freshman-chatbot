package com.cse3111project.bot.spring.category.social;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.InputStream;

import java.util.Scanner;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Societies extends Social {
    public static final String QUERY_KEYWORD[];

    public static final String SOCIETY_KEYWORD[] = { "Chinese Folk Art Society",
                                                     "Chinese Orchestra",
                                                     "Comics and Animation Society",
                                                     "Culinary Art and Culture Society",
                                                     "Drama Society",
                                                     "Film Society",
                                                     "International Cuisine Society",
                                                     "Korean Students’ Association",
                                                     "Magic Club",
                                                     "South Asian Students’ Society",
                                                     "Southeast Asian Students’ Society",
                                                     "Taiwanese Students’Association",
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
                                                     "People’s Campus Radio",
                                                     "Progressust",
                                                     "Rotaract Club of HKUST",
                                                     "Student Air Cadet Society",
                                                     "Student Astronomy Club",
                                                     "Student Social Service Society",
                                                     "The Catholic Society",
                                                     "The Hong Kong Award for Young People User Unit",
                                                     "The Linguistics Society",
                                                     "University YMCA – HKUST",
                                                     "Yo-hoo Club",
                                                     "Sports Association",
                                                     "Archery Club",
                                                     "Cricket Club",
                                                     "Dance Society",
                                                     "Distance Runner’s Club",
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
                                                     "Sport Climbing Students’ Society",
                                                     "Squash Club",
                                                     "Student Basketball Club",
                                                     "Students’ Badminton Club",
                                                     "Students’ Football Club",
                                                     "Students’ Karate Club",
                                                     "Students’ Swimming Club",
                                                     "Table Tennis Club",
                                                     "Tae Kwon Do Club",
                                                     "Tai Chi Society",
                                                     "Tchoukball Club",
                                                     "The Netball Club",
                                                     "Track and Field Club",
                                                     "Volleyball Club",
                                                     "Wing Chun Martial Arts Society",
                                                     "The Undergraduate House One Students’ Association",
                                                     "VERTEX, House II Students’ Association",
                                                     "Glacier, HOUSE III Students’ Association",
                                                     "Vista, House IV Students’ Association",
                                                     "Endeavour, House V Students’ Association",
                                                     "The Business Students’ Union",
                                                     "The Engineering Students’ Union",
                                                     "The Humanities and Social Science Students’ Union",
                                                     "Science Students’ Union",
                                                     "Art Club",
                                                     "Chemistry Students’ Society",
                                                     "Mathematics Students’ Society",
                                                     "International Research Enrichment Students’ Society",
                                                     "The Biology Students’ Society",
                                                     "The Physics Students’ Society",
                                                     "Environment Students’ Society",
                                                     "Risk Management and Business Intelligence Students’ Association",
                                                     "Artiste and Show Production Association",
                                                     "Current Affairs Research Enlightenment",
                                                     "Gym and Fitness Society",
                                                     "The Student Tennis Club",
                                                     "Interdisciplinary Programs Students’ Union,HKUSTSU",
                                                     "Accounting Students’ Society",
                                                     "Information Systems, Business Statistics and Operations Management Students’ Society",
                                                     "Management Students’ Association",
                                                     "The Economics Students’ Society",
                                                     "The Finance Students’ Society",
                                                     "The Marketing Students’ Society",
                                                     "Computer Engineering Students’ Society",
                                                     "Industrial Engineering and Logistics Management Students’ Society",
                                                     "Mechanical Engineering Students’ Association",
                                                     "The Chemical and Biomolecular Engineering Students’ Society",
                                                     "The Civil and Environmental Engineering Students’ Society",
                                                     "The Computer Science and Engineering Students’ Society",
                                                     "The Electronic and Computer Engineering Students’ Society",
                                                     "Biochemistry Students’ Society"
                                                 };

    static {
        QUERY_KEYWORD = Utilities.concatArrays(new String[] { "HKUST society", "HKUST soc", "HKUST societies",
                                                              "UST society", "UST soc", "UST societies" },
                                               SOCIETY_KEYWORD);
    }

    public static final String SOCIETIES_LINK = "https://hkustsucouncil.wordpress.com/standing-committees/the-affiliated-societies-committee/list-of-affiliated-societies/";

    // encapsulation
    private static final String SQL_TABLE = "hkust_societies";
    private static final String STATIC_DATABASE = "/static/social/societiesDatabase.txt";

    // user may query more than 1 societies
    private ArrayList<String> userQuery = null;

    Societies(final ArrayList<String> userQuery){
        this.userQuery = userQuery;  // MUST NOT BE EMPTY
    }

    // used to wrap the society info
    class SocietyInfo {
        private String name;
        private String link;

        // only be visible within this class
        private SocietyInfo(String name, String link){
            this.name = name; this.link = link;
        }

        @Override
        public String toString(){
            return name + ": " + link + '\n';
        }
    }

    // reply societies' link based on user query by searching SQL database
    public String getSocietyWebsiteFromSQL() throws SQLException {
        PreparedStatement SQLQuery = null;
        ResultSet rs = null;
        try {
            ArrayList<SocietyInfo> results = new ArrayList<>();

            // user query MUST NOT be empty
            String SQLStatement = "SELECT name, website_link FROM " + SQL_TABLE +
                                  " WHERE name ILIKE concat(\'%\', ?, \'%\')";
            for (int i = 1; i < userQuery.size(); i++)
                SQLStatement += " OR name ILIKE concat(\'%\', ?, \'%\')";

            log.info("SQLStatement: {}", SQLStatement);

            SQLQuery = SQLDatabase.prepare(SQLStatement);

            for (int i = 0; i < userQuery.size(); i++)
                SQLQuery.setString(i + 1, userQuery.get(i));

            rs = SQLQuery.executeQuery();

            while (rs.next())
                results.add(new SocietyInfo(rs.getString(1), rs.getString(2)));

            return this.replyResults(results);
        }
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (SQLQuery != null)
                    rs.close();
            }
            catch (SQLException e) {
                Utilities.errorLog("Unable to close query statement object", e);
            }
        }
    }

    // reply societies' link based on user query by searching static database
    // only used when SQL database is failed to load
    public String getSocietyWebsiteFromStatic() throws StaticDatabaseFileNotFoundException {
        Scanner staticDatabaseReader = null;
        try {
            ArrayList<SocietyInfo> results = new ArrayList<>();

            // get runtime database file
            InputStream is = this.getClass().getResourceAsStream(STATIC_DATABASE);
            if (is == null)
                throw new StaticDatabaseFileNotFoundException(STATIC_DATABASE + " file not found");

            staticDatabaseReader = new Scanner(is);

            // format:
            // id name website_link
            while (staticDatabaseReader.hasNextLine()){
                String line = staticDatabaseReader.nextLine();
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                String societyName = line.split(",")[1];
                if (userQuery.contains(societyName))
                    results.add(new SocietyInfo(societyName, line.split(",")[2]));
            }

            return this.replyResults(results);
        }
        finally {
            if (staticDatabaseReader != null)
                staticDatabaseReader.close();
        }
    }

    // reply the ultimate results based on user query
    private String replyResults(final ArrayList<SocietyInfo> results){
        StringBuilder replyBuilder = new StringBuilder("Results:\n");
        for (SocietyInfo result : results)
            replyBuilder.append(result.toString());

        return replyBuilder.toString();
    }
}
