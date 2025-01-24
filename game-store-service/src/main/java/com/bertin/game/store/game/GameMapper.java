package com.bertin.game.store.game;

import com.bertin.game.store.category.Category;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

    public GameResponse toGameResponse(Game game) {
        return GameResponse.builder()
                .gameId(game.getId())
                .gameName(game.getTitle())
                // fixme set CDN URL
                .imageUrl("FIX ME")
                .platforms(
                        game.getPlatforms().stream()
                        .map(platform -> platform.getConsole().name())
                        .collect(Collectors.toSet())
                )
                .build();
    }
}
