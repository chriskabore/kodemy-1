package com.bt.dev.kodemy.users.exceptions;

public class RoleInUseException extends RuntimeException {
    public RoleInUseException(String message){
        super(message);
    }
}
