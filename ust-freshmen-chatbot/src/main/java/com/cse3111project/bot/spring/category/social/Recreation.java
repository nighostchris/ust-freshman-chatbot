package com.cse3111project.bot.spring.category.social;

// import com.cse3111project.bot.spring.model.engine.DatabaseEngine;
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

public class Recreation extends Social implements SQLAccessible, StaticAccessible {
    public static final String QUERY_KEYWORD[];

    public static final String AMENITIES_KEYWORD[];
    public static final String LIBRARY_KEYWORD[] = { "library study room", "study room" };
    public static final String AMENITIES_CATEGORY[] = { "Student Amenities Room", "Creativity Room", 
                                                         "Reflection Room", "Mobile Stage", 
                                                         "Temporary Power Supply", "Power Supply",
                                                         "BBQ Facilities", "BBQ Stoves", "BBQ Site", "BBQ",
                                                         "Barbecue Facilities", "Barbecue Stoves", 
                                                         "Barbecue Site", "Barbecue",
                                                         "Darkroom", "Dark Room",
                                                         "Music Room", "Piano Room", 
                                                         "Band Room", "Drum Room",
                                                         "Yangqin and Guzheng",
                                                         "Equipment",  //
                                                         "Promotion Counter", "LG5 Promotion Counter",
                                                         "Tsang Shiu Tim Art Hall",
                                                         "Common Room", 
                                                         "UG Hall 2 Common Room", "Hall 2 Common Room", 
                                                         "UG Hall II Common Room", "Hall II Common Room",
                                                         "Vertex Common Room",
                                                         "UG Hall 6 Common Room", "Hall 6 Common Room",
                                                         "UG Hall VI Common Room", "Hall VI Common Room",
                                                         "LG4 Common Room", "LG5 Common Room",
                                                         "Activity Room", "Conference Room",
                                                         "Multi-function Room", "Workshop",
                                                         "Commual Area", 
                                                         "Lecture Theatre", "Lecture Theater", 
                                                         "Lecture Room", "Classroom"
                                                      };

    // providing general information of amenities booking
    public static final String AMENITIES_BOOKING_REF = "https://sao.ust.hk/facilities/amenities.html";

    // facilities booking system link
    public static final String FBS_LINK = "https://w6.ab.ust.hk/fbs";

    // library room booking system link
    public static final String LRBS_LINK = "http://lbbooking.ust.hk";

    static {
        AMENITIES_KEYWORD = Utilities.concatArrays(new String[] { "amenity", "amenities", "SAO amenities", 
                                                                  "UST amenities", "HKUST amenities", 
                                                                  // matching "recreations"
                                                                  "recreation", 
                                                                  "UST recreation", "HKUST recreation",
                                                                  "facility", "facilities",
                                                                  "UST facilities", "HKUST facilities",
                                                                  "equipment", "UST equipment",
                                                                  "HKUST equipment" },
                                                 AMENITIES_CATEGORY);
        QUERY_KEYWORD = Utilities.concatArrays(new String[] { "booking", "book", "loan" },
                                               LIBRARY_KEYWORD, AMENITIES_KEYWORD);
    }

    // format:
    // name application_link instruction_link
    private static final String SQL_TABLE = "amenities";
    private static final String STATIC_TABLE = "/static/social/amenitiesDatabase.txt";

    // user may have searched multiple recreations
    private ArrayList<String> userQuery;
    private ArrayList<RecreationInfo> results;

    Recreation(final ArrayList<String> userQuery){
        this.userQuery = this.transform(userQuery);
    }

    // transform the alias of amenities from user query
    // e.g. Vertex Common Room == UG Hall 2 Common Room == UG Hall II Common Room
    // so that it could match from SQL / static database
    private ArrayList<String> transform(final ArrayList<String> userQuery){
        ArrayList<String> transformedUserQuery = new ArrayList<>();

        for (String query : userQuery){
            if (query.equals("Vertex Common Room") || query.contains("Hall II Common Room"))
                transformedUserQuery.add("UG Hall 2 Common Room");
            else if (query.contains("Hall VI Common Room"))
                transformedUserQuery.add("UG Hall 6 Common Room");
            else if (query.contains("Barbecue"))
                transformedUserQuery.add("BBQ");
            else if (query.equals("Drum Room"))
                transformedUserQuery.add("Band Room");
            else if (query.equals("Lecture Theater"))
                transformedUserQuery.add("Lecture Theatre");
            else if (query.equals("Lecture Room"))
                transformedUserQuery.add("Classroom");
            else
                transformedUserQuery.add(query);
        }

        return transformedUserQuery;
    }

    // used to wrap the recreation object
    class RecreationInfo {
        private String name;
        private String application_link;  // link of application form
        private String instruction_link;  // link of instructions for booking

        // only visible within this class
        private RecreationInfo(String name, String application_link, String instruction_link){
            this.name = name;
            this.application_link = application_link; this.instruction_link = instruction_link;
        }

        // NOTE that EITHER application_link OR instruction_link MAY be null per recreation, i.e.
        // name | application_link | instruction_link
        //  ... |        ...       |       ...         -- all non-null
        //  ... |       null       |       ...         -- only application_link null
        //  ... |        ...       |       null        -- only instruction_link null
        // since some of recreations only need to walk-in => no need application form
        // while instructions are not provided by SAO website for some recreations 
        // (maybe already mentioned in application form)
        @Override
        public String toString(){
            String recreationInfoBuilder = "for " + name + ",\n";
            if (application_link != null && instruction_link != null)
                recreationInfoBuilder += "You could find application form at " + application_link + " while " +
                                         "its booking instruction could be found at " + instruction_link + '\n';
            else if (application_link == null && instruction_link != null)
                recreationInfoBuilder += "There is no application form but " +
                                         "its booking instruction could be found at " + instruction_link + '\n';
            else if (application_link != null && instruction_link == null)
                recreationInfoBuilder += "You could find application form at " + application_link + " and " +
                                         "its booking instruction should be mentioned in it\n";

            return recreationInfoBuilder;
        }
    }

    // reply booking information and instructions from SQL database
    @Override
    public synchronized String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException {
        try (SQLDatabaseEngine database = new SQLDatabaseEngine(this, SQL_TABLE)) {
            results = new ArrayList<>();

            // userQuery MUST NOT BE EMPTY
            StringBuilder SQLStatement = new StringBuilder("SELECT * FROM ").append(SQL_TABLE)
                                             .append(" WHERE name ILIKE concat(\'%\', ?, \'%\')");
            for (int i = 1; i < userQuery.size(); i++)
                SQLStatement.append(" OR name ILIKE concat(\'%\', ?, \'%\')");

            PreparedStatement SQLQuery = database.prepare(SQLStatement.toString());
            for (int i = 0; i < userQuery.size(); i++)
                SQLQuery.setString(i + 1, userQuery.get(i));

            ResultSet reader = database.executeQuery();

            // format:
            // name application_link instruction_link
            while (reader.next())
                results.add(new RecreationInfo(reader.getString(1), reader.getString(2), reader.getString(3)));

            return super.replyResults(results);
        }
    }

    // reply booking information and instructions from static database
    // only used when the SQL databsae is not accessible
    @Override
    public synchronized String getDataFromStatic() throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException {
        try (StaticDatabaseEngine database = new StaticDatabaseEngine(this, STATIC_TABLE)) {
            results = new ArrayList<>();

            Scanner reader = database.executeQuery();

            while (reader.hasNextLine()){
                String line = reader.nextLine();
                // line starting with # is considered as comment
                if (line.startsWith("#") || line.length() == 0)
                    continue;

                String recreationInfo[] = line.split(",", 3);  // split at most 2 commas (3 parts)
                                                               // and insert empty string if encountered
                for (String query : userQuery){
                    if (recreationInfo[0].toLowerCase().contains(query.toLowerCase())){
                        for (int i = 0; i < recreationInfo.length; i++)
                            if (recreationInfo[i].length() == 0)
                                recreationInfo[i] = null;  // reassign to null for empty string
                            
                            results.add(new RecreationInfo(recreationInfo[0], recreationInfo[1], 
                                                           recreationInfo[2]));
                    }
                }
            }

            return super.replyResults(results);
        }
    }
}