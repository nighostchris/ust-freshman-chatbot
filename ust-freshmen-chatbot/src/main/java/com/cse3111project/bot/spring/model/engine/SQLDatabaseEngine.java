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

/**
 * SQLDatabaseEngine inherits from DatabaseEngine class, which handles the actual communication between client and 
 * SQL server, and retrieve the corresponding data. Connection object is wrapped in this class as well.
 * @version 1.0
 */
@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine implements AutoCloseable 
{
    private static SQLDatabaseEngine database;

    private Connection connection;
    private PreparedStatement query;
    private ResultSet reader;

    /**
     * Constructor of SQLDatabaseEngine
     * @param classObj First parameter taken by this method, which is the Category object.
     * @param SQL_TABLE Second parameter taken by this method, which is the name of table in database.
     * @throws URISyntaxException
     * @throws SQLException
     * @throws NotSQLAccessibleError
     */
    public SQLDatabaseEngine(Object classObj, final String SQL_TABLE) throws URISyntaxException, SQLException, NotSQLAccessibleError 
    {
        super(SQL_TABLE);

        // force to implement SQLAccessible interface in order to use SQLDatabaseEngine
        // ** no need to throw, already handled in SearchEngine.search() **
        if (!(classObj instanceof SQLAccessible))
            throw new NotSQLAccessibleError(classObj.getClass().getName() + " class is not SQLAccessible");

        connection = this.getConnection();
        // SQLConnection = this.getLocalConnection();
    }

    /**
     * This method establish connection with SQL database using JDBC.
     * @return Connection
     * @throws URISyntaxException
     * @throws SQLException
     */
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


	/**
	 * This method is used to test local database connection.
	 * @return Connection
	 * @throws URISyntaxException
	 * @throws SQLException
	 */
    private Connection getLocalConnection() throws URISyntaxException, SQLException 
    {
        // see the DATABASE_URL example in ust-freshmen-chatbot/build.gradle 
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

    /**
     * This method wraps the Connection.prepareStatement() method.
     * @param SQLstatement
     * @return
     * @throws SQLException
     */
    public PreparedStatement prepare(String SQLstatement) throws SQLException 
    {
        this.query = connection.prepareStatement(SQLstatement);
        return this.query;
    }

    /**
     * This method will perform query on SQL database.
     * @return
     * @throws SQLException
     */
    @Override
    public final synchronized ResultSet executeQuery() throws SQLException {
        reader = query.executeQuery();

        return this.reader;
    }

    /**
     * This method will safely close SQL connection and the corresponding query object.
     */
    @Override
    public void close()
    {
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
