package com.bertin.game.store.game;

import com.bertin.game.store.category.Category;
import com.bertin.game.store.category.CategoryRepository;
import com.bertin.game.store.comment.Comment;
import com.bertin.game.store.comment.CommentRepository;
import com.bertin.game.store.common.PageResponse;
import com.bertin.game.store.exception.GameDeletionException;
import com.bertin.game.store.exception.GameNotFoundException;
import com.bertin.game.store.exception.UnsupportedPlatformException;
import com.bertin.game.store.platform.Console;
import com.bertin.game.store.platform.Platform;
import com.bertin.game.store.platform.PlatformRepository;
import com.bertin.game.store.wishlist.Wishlist;
import com.bertin.game.store.wishlist.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
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
    private final CommentRepository commentRepository;
    private final WishlistRepository wishlistRepository;

    public GameService(GameRepository gameRepository, PlatformRepository platformRepository, GameMapper gameMapper, CategoryRepository categoryRepository, GameValidator gameValidator, CommentRepository commentRepository, WishlistRepository wishlistRepository) {
        this.gameRepository = gameRepository;
        this.platformRepository = platformRepository;
        this.gameMapper = gameMapper;
        this.categoryRepository = categoryRepository;
        this.gameValidator = gameValidator;
        this.commentRepository = commentRepository;
        this.wishlistRepository = wishlistRepository;
    }

    public String saveGame(GameRequest gameRequest) {
        log.info("Saving new game: {}", gameRequest.title());
        validateGameRequest(gameRequest);
        final Set<Platform> platforms = getValidatedPlatforms(gameRequest);
        final Game game = createAndSaveGame(gameRequest, platforms);
        log.info("Game saved successfully");
        return game.getId();
    }

    public void updateGame(String gameId, GameRequest partialRequest) {
        log.info("Updating game");
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        updateTitleIfChanged(game, partialRequest);
        updateCategoryIfChanged(game, partialRequest);
        updatePlatformsIfChanged(game, partialRequest);
        gameRepository.save(game);
        log.info("Game updated successfully");
    }

    public String uploadGameImage(MultipartFile file, String gameId) {
        return null;
    }

    public PageResponse<GameResponse> findAllGames(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> gamesPage = gameRepository.findAll(pageable);
        List<GameResponse> gameResponses = gamesPage.stream()
                .map(this.gameMapper::toGameResponse)
                .toList();
        return PageResponse.<GameResponse>builder()
                .content(gameResponses)
                .number(gamesPage.getNumber())
                .size(gamesPage.getSize())
                .totalElements(gamesPage.getTotalElements())
                .totalPages(gamesPage.getTotalPages())
                .isFirst(gamesPage.isFirst())
                .isLast(gamesPage.isLast())
                .build();
    }

    public void deleteGame(String gameId, boolean confirm) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found"));

        long commentCount = commentRepository.countByGameId(gameId);
        long wishlistCount = wishlistRepository.countByGameId(gameId);

        final List<String> warningMessages = new ArrayList<>();
        if(commentCount > 0){
            warningMessages.add("Comment count is greater than 0");
        }

        if(wishlistCount > 0){
            warningMessages.add("Wishlist count is greater than 0");
        }

        if (!warningMessages.isEmpty() && !confirm) {
            throw new GameDeletionException(warningMessages);
        }

        log.info("Unlinking game association");
        wishlistRepository.unlinkGameAssociation(gameId);

        log.info("Deleting game");
        gameRepository.delete(game);
        log.info("Game deleted successfully");

        game.getWishlists().clear();
    }


    private void validateGameRequest(GameRequest gameRequest) {
        gameValidator.validateGameTitleUnique(gameRequest.title());
        gameValidator.validateCategoryExists(gameRequest.categoryId());
    }

    private Set<Platform> getValidatedPlatforms(GameRequest gameRequest) {
        final Set<Console> selectedConsoles = gameRequest.platforms().stream()
                .map(platformName -> {
                    try {
                        return Console.valueOf(platformName);
                    } catch (IllegalArgumentException e) {
                        throw new UnsupportedPlatformException("Unknown console" + platformName);
                    }
                })
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

    private void updateTitleIfChanged(Game game, GameRequest partialRequest) {
        if (partialRequest.title() != null && !partialRequest.title().equals(game.getTitle())) {
            gameValidator.validateGameTitleUnique(partialRequest.title());
            game.setTitle(partialRequest.title());
        }
    }

    private void updateCategoryIfChanged(Game game, GameRequest partialRequest) {
        if (partialRequest.categoryId() != null && !partialRequest.categoryId().equals(game.getCategory().getId())) {
            gameValidator.validateCategoryExists(partialRequest.categoryId());
            final Category newCategory = Category.builder()
                    .id(partialRequest.categoryId())
                    .build();
            game.setCategory(newCategory);
        }
    }

    private void updatePlatformsIfChanged(Game game, GameRequest partialRequest) {
        if (partialRequest.platforms() != null) {
            final Set<Platform> newPlatforms = getValidatedPlatforms(partialRequest);

            final List<Platform> currentPlatforms = new ArrayList<>(game.getPlatforms());
            final List<Platform> platformsToAdd = new ArrayList<>(newPlatforms);
            platformsToAdd.removeAll(currentPlatforms);
            final List<Platform> platformsToRemove = new ArrayList<>(currentPlatforms);
            platformsToRemove.removeAll(newPlatforms);

            for (Platform p : platformsToAdd) {
                game.addPlatform(p);
            }
            for (Platform p : platformsToRemove) {
                game.removePlatform(p);
            }
        }
    }

}
