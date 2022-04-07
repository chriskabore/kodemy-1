package com.bt.dev.kodemy.users.exceptions;

public class InvalidPermissionDataException extends RuntimeException {
    public InvalidPermissionDataException(String message){
        super(message);
    }
}
