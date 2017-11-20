package com.cse3111project.bot.spring.model.engine;

import com.cse3111project.bot.spring.model.engine.marker.StaticAccessible;

import java.io.InputStream;
import java.io.IOException;

import java.util.Scanner;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

/**
 * StaticDatabaseEngine inherits from DatabaseEngine class, which handles the actual communication between client and 
 * static database, and retrieve the corresponding data.
 * @version 1.0
 */
public class StaticDatabaseEngine extends DatabaseEngine implements AutoCloseable 
{
    private InputStream buffer;  // storing static database buffer
    private Scanner reader;

    /**
     * Constructor for StaticDatabaseEngine
     * @param classObj First parameter taken by this method, which is the Category object.
     * @param STATIC_TABLE Second parameter taken by this method, which is the name of static database.
     * @throws NotStaticAccessibleError
     */
    public StaticDatabaseEngine(Object classObj, final String STATIC_TABLE) throws NotStaticAccessibleError
    {
        super(STATIC_TABLE);

        // force to implement StaticAccessible interface in order to use StaticDatabaseEngine
        if (!(classObj instanceof StaticAccessible))
            throw new NotStaticAccessibleError(classObj + " is not StaticAccessible");
    }

    /**
     * This method will perform query on static database.
     * @return Scanner
     * @throws StaticDatabaseFileNotFoundException
     */
    @Override
    public final synchronized Scanner executeQuery() throws StaticDatabaseFileNotFoundException {
        this.buffer = this.getClass().getResourceAsStream(TABLE);
        if (this.buffer == null)
            throw new StaticDatabaseFileNotFoundException(TABLE + " file not found");

        this.reader = new Scanner(this.buffer);

        return this.reader;
    }

    /**
     * This method will close the internal buffer.
     */
    @Override
    public void close(){
        try {
            if (buffer != null)
                buffer.close();
            if (reader != null)
                reader.close();
        }
        // just suppress the error since indeed the query result can be found
        catch (IOException e) {
            Utilities.errorLog("Unable to close static database", e);
        }
    }
}
