package com.bt.dev.kodemy.users.exceptions;

public class InvalidRoleIdentifierException extends RuntimeException {

    public InvalidRoleIdentifierException(String message){
        super(message);
    }
}
