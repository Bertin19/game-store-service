package com.bertin.game.store.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class GameDeletionException extends RuntimeException {
    private final List<String> warnings;

    public GameDeletionException(List<String> warnings) {
        super("Cannot delete game due to existing dependencies.");
        this.warnings = warnings;
    }

}
