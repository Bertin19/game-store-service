package com.bertin.game.store.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    @Modifying
    @Query(value = "DELETE FROM game_wishlist WHERE game_id = :gameId", nativeQuery = true)
    void unlinkGameAssociation(String gameId);

    @Query("""
           SELECT COUNT(wishlist) FROM Wishlist wishlist
           JOIN wishlist.games game
           WHERE game.id = :gameId
           """)
    long countByGameId(String gameId);
}
