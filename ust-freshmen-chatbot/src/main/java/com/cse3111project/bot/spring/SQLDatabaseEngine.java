package com.cse3111project.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {  // load database deployed from Heroku
	@Override
	String search(String text) throws Exception {  // handle Exceptions in caller method
        Connection SQLDatabase = null;  // may be moved to constructor?
        PreparedStatement SQLQuery = null;  // precompiled SQL statement
        ResultSet rs = null;  // query result from SQL database
        String result = null;  // prepare to store the result
        try {
            SQLDatabase = this.getConnection();  // get connection from online database
            // String SQLTable = "Response";  // table name cannot be replaced by ? *****
            // NOTE that SQL is case-insensitive, therefore need to use double quotes to enclose
            //                                    if want to be case-sensitive
            // get the entire table
            SQLQuery = SQLDatabase.prepareStatement("SELECT * FROM \"Response\"");
            // SQLQuery.setString(1, SQLTable);  *****

            rs = SQLQuery.executeQuery();

            //        Response
            //  keyword  |  response
            // read the entire table, always return the first match (can be modified)
            while (result == null && rs.next()){
                // System.err.println(rs.getString(1));
                if (text.toLowerCase().equals(rs.getString(1).toLowerCase()))  // exact match
                    result = rs.getString(2);
                if (text.toLowerCase().contains(rs.getString(1).toLowerCase()))  // partial match
                    result = rs.getString(2);  // only get the first result (can be modified)
            }
        }
        catch (URISyntaxException e){
            log.info("Wrong URI: {}", e.toString());
        }
        catch (SQLTimeoutException e){
            log.info("Timeout: SQL statement expired, abort execution:: {}", e.toString());
        }
        catch (SQLException e){
            log.info("Unable to connect database: {}", e.toString());
        }
        finally {  // safe close
            try {
                if (rs != null)
                    rs.close();
                if (SQLQuery != null)
                    SQLQuery.close();
                if (SQLDatabase != null)
                    SQLDatabase.close();
            }
            catch (SQLException e){
                log.info("Unable to close database: {}", e.toString());
            }
        }

        if (result != null) return result;
        throw new Exception("NOT FOUND");
	}
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
        // get database URL from environment variable, needs to be set in build.gradle
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() 
                            + "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
}
