package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.Getter;

@Getter
public class CustomError extends RuntimeException {
    private final int status;
    public CustomError(int status, String message) {
        super(message);
        this.status = status;
    }
}
