package com.cse3111project.bot.spring.category.academic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLTimeoutException;
import java.sql.SQLException;

import com.cse3111project.bot.spring.SQLDatabaseEngine;

import java.net.URISyntaxException;

import java.io.InputStream;

import java.util.Scanner;
import java.util.ArrayList;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

import lombok.extern.slf4j.Slf4j;

// Staff Category
@Slf4j
public class Staff extends Academic {
    private static final String SQL_TABLE = "staff";
    private static final String STATIC_DATABASE = "/static/academic/staffDatabase.txt";

    // temporarily just consider queried staff name
    // private String department;  // MAY USE hashmap in the future ***
    private ArrayList<String> queryStaffNameList;

    public Staff(final ArrayList<String> queryStaffNameList){
        this.queryStaffNameList = queryStaffNameList;
    }

    public static final String STAFF_NAME_KEYWORD[];

    public static final String STAFF_POSITION_KEYWORD[] = { "Professor", "Prof.", "TA", "Teaching Assistant",
                                                            "Lecturer", "Instructor", "Teacher" };

    // Temporary solution:  *****
    // initialize QUERY_KEYWORD from SQL database
    // After tried everything, hard coding is the best way
    static {
        STAFF_NAME_KEYWORD = new String[] { "Yang Qiang", "Chung Chi Shing Albert",
                                            "Chan Tony F.", "Cheng Kwang Ting Tim",
                                            "Li Bo", "Chan Shueng Han Gary",
                                            "Chan Lei", "Cheng Siu Wing",
                                            "Cheung Shing Chi", "Ding Cun Sheng",
                                            "Golin Mordecai J.", "Horner Andrew B.",
                                            "Kwok Tin Yau James", "Lee Dik Lun",
                                            "Lin Fang Zhen", "Papadias Dimitris",
                                            "Pong Ting Chuen", "Qu Hua Min",
                                            "Quan Long", "Tai Chiew Lan",
                                            "Tang Chi Keung", "Wu De Kai",
                                            "Yeung Dit Yan", "Zhang Lian Wen Nevin",
                                            "Arya Sunil", "Bensaou Brahim",
                                            "Chen Kai", "Kim Sung Hun",
                                            "Luo Qiong", "Mak Kan Wing Brian",
                                            "Muppala Jogesh K.", "Ng Siu Hung Wilfred",
                                            "Sander Pedro", "Wong Chi Wing Raymond",
                                            "Yi Ke", "Zhang Charles",
                                            "Hui Pan", "Liu Ming",
                                            "Ma Xiao Juan", "Papadopoulos Dimitris",
                                            "Song Yang Qiu", "Wang Tao",
                                            "Wang Wei", "Hang Kai Yu",
                                            "Zhang Yu", "Ieong Ricci",
                                            "Rossiter David Paul", "Li Xin",
                                            "Chan Ki Cecia", "Gibson Lam",
                                            "Lam Ngok", "Leung Wai Ting",
                                            "Desmond Tsoi Yau Chat"
                                          };
    }

    // check whether contains last name if specified staff position, e.g. Professor, Instructor, ...
    // if so, append to matchedResults
    // NOTE that full name should be found by partial match method in SearchEngine.parse()
    // @param userQuery: omitted symbols (!@#$%...) + toLowerCase()
    public static void containsLastName(String userQuery, ArrayList<String> matchedResults){
        ArrayList<String> newResults = new ArrayList<>();

        for (String result : matchedResults){
            for (String staffPositionKeyword : STAFF_POSITION_KEYWORD){
                if (result.equals(staffPositionKeyword)){
                    int i = 0; int j = 0;
                    // attempt to find all last names, which should be after staff position (assumed)
                    while (true) {
                        // find that word
                        i = userQuery.indexOf(staffPositionKeyword.toLowerCase(), j);
                        if (i == -1) break;  // not found
                        // find <Space> after that word
                        i = userQuery.indexOf(' ', i);
                        // skip all <Space>s in between and find non-whitespace character
                        for (; i < userQuery.length() && userQuery.charAt(i) == ' '; i++);
                        if (i == userQuery.length()) break;  // all whitespaces at the end after position
                        // find the next <Space> to locate lastName, if have
                        j = userQuery.indexOf(' ', i);
                        // extract last name
                        String lastName = (j == -1 ? userQuery.substring(i) : userQuery.substring(i, j));

                        log.info("lastName: {}", lastName);

                        // find all matched results to the last name
                        // ** NOTE that if modified as  matchedResults.add()
                        // while iterating in for loop => easily get unsynchronized
                        // ==> throw ConcurrentModificationException
                        for (String staffNameKeyword : STAFF_NAME_KEYWORD){
                            // if match last name
                            if (staffNameKeyword.toLowerCase().split(" ")[0].equals(lastName)){
                                if (matchedResults.contains(staffNameKeyword))  // already matched full name
                                    break;
                                else
                                    newResults.add(staffNameKeyword);  // **
                            }
                        }
                        Utilities.arrayLog("current new results", newResults);

                        if (j == -1) break;
                    }
                }
            }
        }

        // finally append new results
        for (String newResult : newResults)
            matchedResults.add(newResult);
    } 

    // used to wrap staff object
    class StaffInfo {
        private String name;
        private String position;
        private String officeLocation;
        private String email;

        private StaffInfo(String name, String position, String officeLocation, String email){
            this.name = name;
            this.position = position;
            this.officeLocation = officeLocation;
            this.email = email;
        }

        @Override
        public String toString(){
            return "Office of " + name + ", " + position + ", is located at " + officeLocation + '\n' + 
                   "Email: " + email + '\n';
        }
    }

    // search by staff name(s)
    // return staff contact(s) using SQL database
    public String getContactInfoFromSQL() throws SQLException {
        ArrayList<StaffInfo> results = new ArrayList<>();

        Utilities.arrayLog("query staff list", queryStaffNameList);

        for (String userQuery : this.queryStaffNameList){
            PreparedStatement SQLQuery = null;
            ResultSet rs = null;
            try {
                // StringBuilder for PERFORMANCE
                String SQLStatement = new StringBuilder("SELECT * FROM ").append(SQL_TABLE)
                                          .append(" WHERE staffName LIKE concat(\'%\', ?, \'%\')")
                                          .toString();

                SQLQuery = SQLDatabase.prepare(SQLStatement);

                SQLQuery.setString(1, userQuery);

                rs = SQLQuery.executeQuery();

                // format:
                // department staffName position officeLocation email
                while (rs.next()){
                    if (userQuery.equals(rs.getString(2)))
                        results.add(new StaffInfo(rs.getString(2), rs.getString(3), 
                                                  rs.getString(4), rs.getString(5)));
                }
            }
            finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (SQLQuery != null)
                        SQLQuery.close();
                }
                catch (SQLException e) {
                    Utilities.errorLog("Unable to close query statement object", e);
                }
            }
        }

        // ONLY happens when the SQL database and static database are NOT synchronized
        if (results.isEmpty())
            return "Specified staff not found, sorry";

        return this.replyResults(results);
    }

    // return staff contact(s) using static database
    // only used when fail to connect SQL database
    public String getContactInfoFromStatic() throws StaticDatabaseFileNotFoundException {
        Scanner staffReader = null;
        ArrayList<StaffInfo> results = new ArrayList<>();
        try {
            InputStream is = this.getClass().getResourceAsStream(STATIC_DATABASE);
            if (is == null)
                throw new StaticDatabaseFileNotFoundException(STATIC_DATABASE + " file not found");

            staffReader = new Scanner(is);

            while (staffReader.hasNextLine()){
                // format:
                // department staffName position officeLocation email
                String line = staffReader.nextLine();

                if (line.startsWith("#") || line.length() == 0)
                    continue;

                String staffName = line.split(",")[1];
                for (String userQuery : queryStaffNameList){
                    if (userQuery.equals(staffName))
                        results.add(new StaffInfo(line.split(",")[1], line.split(",")[2],
                                                  line.split(",")[3], line.split(",")[4]));
                }
            }
        }
        finally {
            if (staffReader != null)  // safe .close()
                staffReader.close();
        }

        // SHOULD NOT HAPPEN
        if (results.isEmpty())
            return "Specified staff not found, sorry";

        return this.replyResults(results);
    }

    // reply the ultimate result searched from SQL / static database
    // format:
    // Results:
    // <staff 1 info>
    // 
    // <staff 2 info>
    // ...
    private String replyResults(ArrayList<StaffInfo> results){
        StringBuilder replyBuilder = new StringBuilder("Results:\n");
        for (int i = 0; i < results.size(); i++){
            replyBuilder.append(results.get(i).toString());
            if (i != results.size() - 1)
                replyBuilder.append("\n");
        }

        return replyBuilder.toString();
    }
}
