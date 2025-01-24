package com.bertin.game.store.game;

import com.bertin.game.store.category.CategoryRepository;
import com.bertin.game.store.exception.CategoryNotFoundException;
import com.bertin.game.store.exception.GameAlreadyExistsException;
import org.springframework.stereotype.Component;

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
            throw new CategoryNotFoundException("Category does not exist");
        }
    }

    public void validateGameTitleUnique(String title) {
        if (gameRepository.existsByTitle(title)) {
            throw new GameAlreadyExistsException("Game title already exists");
        }
    }
}
