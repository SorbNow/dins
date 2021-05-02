package com.sorb.dins.exception;

public class ExistsInDatabaseException extends RuntimeException {
    public ExistsInDatabaseException(String message) {
        super(message);
    }
}
