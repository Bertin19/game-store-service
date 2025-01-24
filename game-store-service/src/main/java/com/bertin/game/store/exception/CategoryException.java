package com.bertin.game.store.exception;

import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {
    private final String errorCode;

    public CategoryException(String message) {
        super(message);
        this.errorCode = null;
    }

    public CategoryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}


