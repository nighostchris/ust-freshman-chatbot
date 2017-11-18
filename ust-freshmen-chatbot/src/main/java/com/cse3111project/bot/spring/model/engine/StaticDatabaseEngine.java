package com.cse3111project.bot.spring.model.engine;

import com.cse3111project.bot.spring.model.engine.marker.StaticAccessible;

import java.io.InputStream;
import java.io.IOException;

import java.util.Scanner;
import com.cse3111project.bot.spring.utility.Utilities;

import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

public class StaticDatabaseEngine extends DatabaseEngine implements AutoCloseable {
    private InputStream buffer;  // storing static database buffer
    private Scanner reader;

    public StaticDatabaseEngine(Object classObj, final String STATIC_TABLE) throws NotStaticAccessibleError {
        super(STATIC_TABLE);

        // force to implement StaticAccessible interface in order to use StaticDatabaseEngine
        if (!(classObj instanceof StaticAccessible))
            throw new NotStaticAccessibleError(classObj + " is not StaticAccessible");
    }

    // load static database file
    @Override
    public final synchronized Scanner executeQuery() throws StaticDatabaseFileNotFoundException {
        this.buffer = this.getClass().getResourceAsStream(TABLE);
        if (this.buffer == null)
            throw new StaticDatabaseFileNotFoundException(TABLE + " file not found");

        this.reader = new Scanner(this.buffer);

        return this.reader;
    }

    // close the internal buffer
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
