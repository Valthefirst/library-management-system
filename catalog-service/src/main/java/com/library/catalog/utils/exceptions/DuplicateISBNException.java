package com.library.catalog.utils.exceptions;

public class DuplicateISBNException extends RuntimeException{

    public DuplicateISBNException() {
        super();
    }

    public DuplicateISBNException(String message) {
        super(message);
    }

    public DuplicateISBNException(Throwable cause) {
        super(cause);
    }

    public DuplicateISBNException(String message, Throwable cause) {
        super(message, cause);
    }
}
