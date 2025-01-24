package com.bertin.game.store.game;

import java.util.Set;

public record GameRequest(
        String title, // perform a check, to not allow duplicates
        String categoryId, // check for category existence, because of relationship with this entity/table
        Set<String> platforms // check for platform existence, because of relationship with this entity/table
) {
}
