package com.library.apigateway.utils.exceptions;

public class UnavailableBookException extends RuntimeException{

    public UnavailableBookException() {}

    public UnavailableBookException(String message) { super(message); }

    public UnavailableBookException(Throwable cause) { super(cause); }

    public UnavailableBookException(String message, Throwable cause) { super(message, cause); }
}
