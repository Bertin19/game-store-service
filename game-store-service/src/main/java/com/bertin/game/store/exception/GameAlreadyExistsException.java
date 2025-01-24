package com.bertin.game.store.exception;

public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(String message) {
        super(message);
    }
}

