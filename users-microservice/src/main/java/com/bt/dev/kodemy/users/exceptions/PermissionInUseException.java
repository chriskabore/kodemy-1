package com.bt.dev.kodemy.users.exceptions;

public class PermissionInUseException extends RuntimeException {
    public PermissionInUseException(String message){
        super(message);
    }
}
