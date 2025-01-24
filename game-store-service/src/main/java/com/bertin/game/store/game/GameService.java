package com.bertin.game.store.game;

import com.bertin.game.store.category.CategoryRepository;
import com.bertin.game.store.common.PageResponse;
import com.bertin.game.store.exception.UnsupportedPlatformException;
import com.bertin.game.store.platform.Console;
import com.bertin.game.store.platform.Platform;
import com.bertin.game.store.platform.PlatformRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final GameMapper gameMapper;
    private final CategoryRepository categoryRepository;
    private final GameValidator gameValidator;

    public GameService(GameRepository gameRepository, PlatformRepository platformRepository, GameMapper gameMapper, CategoryRepository categoryRepository, GameValidator gameValidator) {
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
        this.gameMapper = gameMapper;
        this.categoryRepository = categoryRepository;
        this.gameValidator = gameValidator;
    }

    public String saveGame(GameRequest gameRequest) {
        validateGameRequest(gameRequest);
        final Set<Platform> platforms = getValidatedPlatforms(gameRequest);
        final Game game = createAndSaveGame(gameRequest, platforms);
        return game.getId();
    }

    public void updateGame(String gameId, GameRequest gameRequest) {
    }

    public String uploadGameImage(MultipartFile file, String gameId) {
        return null;
    }

    public PageResponse<GameResponse> findAllGames(int page, int size) {
        return null;
    }

    public void deleteGame(String gameId) {
    }

    private void validateGameRequest(GameRequest gameRequest) {
        gameValidator.validateGameTitleUnique(gameRequest.title());
        gameValidator.validateCategoryExists(gameRequest.categoryId());
    }
    
    private Set<Platform> getValidatedPlatforms(GameRequest gameRequest) {
        final Set<Console> selectedConsoles = gameRequest.platforms().stream()
                .map(Console::valueOf)
                .collect(Collectors.toSet());
        final Set<Platform> platforms = platformRepository.findAllConsoleIn(selectedConsoles);
        validatePlatformsExist(platforms, selectedConsoles);
        return platforms;
    }

    private void validatePlatformsExist(Set<Platform> platforms, Set<Console> selectedConsoles) {
        if (platforms.size() != selectedConsoles.size()) {
            throw new UnsupportedPlatformException("There are platforms that are not supported for this game");
        }
    }

    private Game createAndSaveGame(GameRequest gameRequest, Set<Platform> platforms) {
        final Game game = gameMapper.toGame(gameRequest);
        game.setPlatforms(platforms.stream().toList());
        return gameRepository.save(game);
    }
}
