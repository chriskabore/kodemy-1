package com.bt.dev.kodemy.users.exceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message){
        super(message);
    }
}
