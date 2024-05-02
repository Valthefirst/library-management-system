package com.library.catalog.utils.exceptions;

public class InvalidISBNException extends RuntimeException{

    public InvalidISBNException() {}

    public InvalidISBNException(String message) { super(message); }

    public InvalidISBNException(Throwable cause) { super(cause); }

    public InvalidISBNException(String message, Throwable cause) { super(message, cause); }
}
