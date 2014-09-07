package com.btmatthews.atlas.core.dao;

public class DataAccessException extends RuntimeException {

    public DataAccessException(final String message) {
        super(message);
    }

    public DataAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
