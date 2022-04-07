package com.bt.dev.kodemy.users.exceptions;

public class InvalidRoleDataException extends RuntimeException {
    public InvalidRoleDataException(String message){
        super(message);
    }
}
