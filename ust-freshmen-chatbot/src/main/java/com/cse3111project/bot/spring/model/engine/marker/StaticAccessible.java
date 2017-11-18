package com.cse3111project.bot.spring.model.engine.marker;

import com.cse3111project.bot.spring.exception.NotStaticAccessibleError;
import com.cse3111project.bot.spring.exception.StaticDatabaseFileNotFoundException;

// implements StaticAccessible => static database is available to that class
public interface StaticAccessible {
    // perform each query at a single thread
    public abstract String getDataFromStatic() throws NotStaticAccessibleError, StaticDatabaseFileNotFoundException;
}
