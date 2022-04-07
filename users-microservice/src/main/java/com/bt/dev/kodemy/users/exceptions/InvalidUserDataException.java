package com.bt.dev.kodemy.users.exceptions;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException (String message){
        super(message);
    }
}
