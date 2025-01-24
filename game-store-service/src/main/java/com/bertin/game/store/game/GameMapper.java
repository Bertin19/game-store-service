package com.bertin.game.store.game;

import com.bertin.game.store.category.Category;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    public Game toGame(GameRequest gameRequest) {
        return Game.builder()
                .title(gameRequest.title())
                .category(
                        Category.builder()
                                .id(gameRequest.categoryId()
                                ).build()
                )
                .build();
    }
}
