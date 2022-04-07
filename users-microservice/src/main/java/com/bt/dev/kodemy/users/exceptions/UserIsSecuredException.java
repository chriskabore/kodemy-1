package com.bt.dev.kodemy.users.exceptions;

public class UserIsSecuredException extends RuntimeException {
    public UserIsSecuredException(String message){
        super(message);
    }
}
