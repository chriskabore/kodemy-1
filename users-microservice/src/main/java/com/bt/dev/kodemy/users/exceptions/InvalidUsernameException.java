package com.bt.dev.kodemy.users.exceptions;

public class InvalidUsernameException extends RuntimeException{
    public InvalidUsernameException(String message){
        super(message);
    }
}
