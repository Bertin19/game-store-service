package com.bertin.game.store.exception;

import lombok.Getter;

@Getter
public class GameException extends RuntimeException {
    private final String errorCode;

    public GameException(String message) {
        super(message);
        this.errorCode = null;
    }

    public GameException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
