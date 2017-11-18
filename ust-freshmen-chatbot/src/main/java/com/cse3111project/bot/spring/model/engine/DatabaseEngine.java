package com.cse3111project.bot.spring.model.engine;

import java.sql.PreparedStatement;

public abstract class DatabaseEngine {
    // table name / filepath
    protected String TABLE;

    protected DatabaseEngine(String TABLE) {
        this.TABLE = TABLE;
    }

    // retrieve database table name
    public final String getTableName() { return this.TABLE; }

    // execute each query at a single thread
    public abstract Object executeQuery() throws Exception;
}
