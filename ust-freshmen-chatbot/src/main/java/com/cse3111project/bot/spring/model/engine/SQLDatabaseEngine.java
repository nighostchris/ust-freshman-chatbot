package com.cse3111project.bot.spring.model.engine;

import com.cse3111project.bot.spring.model.engine.marker.SQLAccessible;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.net.URI;
import java.net.URISyntaxException;

import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;

import lombok.extern.slf4j.Slf4j;  // logging

// this class wraps the SQL connection
@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine implements AutoCloseable {
    private static SQLDatabaseEngine database;

    private Connection connection;
    private PreparedStatement query;
    private ResultSet reader;

    public SQLDatabaseEngine(Object classObj, final String SQL_TABLE) throws URISyntaxException, SQLException, NotSQLAccessibleError {
        super(SQL_TABLE);

        // force to implement SQLAccessible interface in order to use SQLDatabaseEngine
        // ** no need to throw, already handled in SearchEngine.search() **
        if (!(classObj instanceof SQLAccessible))
            throw new NotSQLAccessibleError(classObj.getClass().getName() + " class is not SQLAccessible");

        connection = this.getConnection();
        // SQLConnection = this.getLocalConnection();
    }

    // establish connection with SQL database using JDBC
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
    public PreparedStatement prepare(String SQLstatement) throws SQLException {
        this.query = connection.prepareStatement(SQLstatement);
        
        return this.query;
    }

    // perform query on SQL database
    @Override
    public final synchronized ResultSet executeQuery() throws SQLException {
        reader = query.executeQuery();

        return this.reader;
    }

    // safely .close() SQL connection and query object
    @Override
    public void close(){
        try {
            if (reader != null)
                reader.close();
            if (query != null)
                query.close();
            if (connection != null)
                connection.close();
        }
        // just suppress the error since indeed the query result can be found
        catch (SQLException e) {
            Utilities.errorLog("Unable to close SQL database / query object", e);
        }
    }
}
