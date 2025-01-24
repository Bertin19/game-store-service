package com.bertin.game.store.exception;

import lombok.Getter;

@Getter
public class PlatformException extends RuntimeException {
    private final String errorCode;

    public PlatformException(String message) {
        super(message);
        this.errorCode = null;
    }

    public PlatformException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}