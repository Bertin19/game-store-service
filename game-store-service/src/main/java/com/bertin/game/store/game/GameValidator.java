package com.bertin.game.store.game;

import com.bertin.game.store.category.CategoryRepository;
import com.bertin.game.store.exception.GameException;
import org.springframework.stereotype.Component;

import static com.bertin.game.store.exception.ErrorCodes.CATEGORY_NOT_FOUND;
import static com.bertin.game.store.exception.ErrorCodes.GAME_ALREADY_EXISTS;

@Component
public class GameValidator {
    private final CategoryRepository categoryRepository;
    private final GameRepository gameRepository;

    public GameValidator(CategoryRepository categoryRepository, GameRepository gameRepository) {
        this.categoryRepository = categoryRepository;
        this.gameRepository = gameRepository;
    }

    public void validateCategoryExists(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new GameException("Category does not exist", CATEGORY_NOT_FOUND);
        }
    }

    public void validateGameTitleUnique(String title) {
        if (gameRepository.existsByTitle(title)) {
            throw new GameException("Game title already exists", GAME_ALREADY_EXISTS);
        }
    }
}
