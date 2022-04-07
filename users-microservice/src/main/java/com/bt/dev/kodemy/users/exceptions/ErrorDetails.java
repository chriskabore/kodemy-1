package com.bt.dev.kodemy.users.exceptions;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorDetails implements Serializable {

    private final String message;

    private final long timestamp;

    public ErrorDetails(final String message) {
        this.timestamp = System.currentTimeMillis();
        this.message = message;
    }
}
