package com.bt.dev.kodemy.users.exceptions;

public class PermissionNotFoundException extends RuntimeException {

    public PermissionNotFoundException(String message){
        super(message);
    }
}
