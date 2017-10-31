package com.cse3111project.bot.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;  // logging

// this class wraps the SQL connection for better encapsulation
@Slf4j
public class SQLDatabaseEngine {
    private Connection SQLConnection = null;

    // only SearchEngine can use
    SQLDatabaseEngine() throws URISyntaxException, SQLException {
        SQLConnection = this.getConnection();
        // SQLConnection = this.getLocalConnection();
    }

    // establish connection with SQL database using JDBC
    // may be moved to Utilities class
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection = null;
        // get database URL from environment variable, need to be set in build.gradle
        // using URI constructor to check if the DATABASE_URL has valid URI syntax
		URI databaseURI = new URI(System.getenv("DATABASE_URL"));

		String username = databaseURI.getUserInfo().split(":")[0];
		String password = databaseURI.getUserInfo().split(":")[1];
		String databaseURL = "jdbc:postgresql://" + 
                             databaseURI.getHost() + ':' + databaseURI.getPort() + databaseURI.getPath() +
                             "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info("databaseURL: {}", databaseURL);
		
		connection = DriverManager.getConnection(databaseURL, username, password);

		return connection;
	}

    // local connection for testing
    // see the DATABASE_URL example in ust-freshmen-chatbot/build.gradle 
    private Connection getLocalConnection() throws URISyntaxException, SQLException {
        Connection connection = null;

        URI databaseURI = new URI(System.getenv("DATABASE_URL"));

        String username = "cnleungaa";  // modify your own username
        String password = "12345";      // modify your own password
        String databaseURL = "jdbc:" + databaseURI.toString();

		log.info("Username: {} Password: {}", username, password);
		log.info("databaseURL: {}", databaseURL);

		connection = DriverManager.getConnection(databaseURL, username, password);
        
        return connection;
    }

    // wraps the Connection.prepareStatement() method
    public PreparedStatement prepare(String SQLStatement) throws SQLException {
        return SQLConnection.prepareStatement(SQLStatement);
    }

    // safely .close() connection to SQL
    public void closeConnection() throws SQLException {
        if (SQLConnection != null){
            SQLConnection.close();
            SQLConnection = null;  // avoid dangling reference
        }
    }
}
