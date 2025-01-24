package com.bertin.game.store.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("""
           SELECT comment FROM Comment comment
           WHERE comment.game.id = :gameId
           """
    )
    List<Comment> findByGameId(String gameId);

    @Query("""
           SELECT COUNT(comment) FROM Comment comment
           WHERE comment.game.id = :gameId
           """)
    long countByGameId(String gameId);
}
