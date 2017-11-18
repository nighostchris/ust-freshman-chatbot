package com.cse3111project.bot.spring.model.engine.marker;

import java.sql.SQLException;

import java.net.URISyntaxException;

import com.cse3111project.bot.spring.exception.NotSQLAccessibleError;

// implements SQLAccessible => SQL database is available for that class
public interface SQLAccessible {
    // execute each SQL query at a single thread
    public abstract String getDataFromSQL() throws NotSQLAccessibleError, URISyntaxException, SQLException;
}
