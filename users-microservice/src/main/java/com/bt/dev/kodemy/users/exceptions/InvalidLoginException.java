package com.bt.dev.kodemy.users.exceptions;

public class InvalidLoginException extends RuntimeException {

    public InvalidLoginException(String message){
        super(message);
    }
}
