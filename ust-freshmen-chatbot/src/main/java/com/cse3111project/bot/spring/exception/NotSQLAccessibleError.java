package com.cse3111project.bot.spring.exception;

// thrown when the class does not implements SQLAccessible while initializing SQLDatabaseEngine
// it is considered as an internal error
@SuppressWarnings("serial")
public class NotSQLAccessibleError extends Error {
    public NotSQLAccessibleError(String errMsg) {
        super(errMsg);
    }
}
