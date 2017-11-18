package com.cse3111project.bot.spring.exception;

// thrown when the class does not implements StaticAccessible while initializing StaticDatabaseEngine
// it is considered as an internal error
@SuppressWarnings("serial")
public class NotStaticAccessibleError extends Error {
    public NotStaticAccessibleError(String errMsg) {
        super(errMsg);
    }
}
