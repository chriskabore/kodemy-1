package com.bt.dev.kodemy.users.exceptions;

public class InvalidUserIdentifierException extends RuntimeException {
    public InvalidUserIdentifierException(String message){
        super(message);
    }
}
