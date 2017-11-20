package com.cse3111project.bot.spring.model.engine;

import java.sql.PreparedStatement;

/**
 * DatabaseEngine abstract class defines a basic structure for both SQL database and static database.
 * @version 1.0
 */
public abstract class DatabaseEngine 
{
    // table name / filepath
    protected String TABLE;

    /**
     * Constructor of DatabaseEngine class.
     * @param TABLE It is the table name in the database
     */
    protected DatabaseEngine(String TABLE) 
    {
        this.TABLE = TABLE;
    }

    /**
     * Getter method for instance variable TABLE
     * @return String This method returns the database table name
     */
    public final String getTableName() { return this.TABLE; }

    /**
     * Abstract method which execute query at a single thread.
     * @return Object
     * @throws Exception
     */
    public abstract Object executeQuery() throws Exception;
}
