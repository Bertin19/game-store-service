package com.bertin.game.store.game;

import com.bertin.game.store.category.Category;
import com.bertin.game.store.comment.Comment;
import com.bertin.game.store.common.BaseEntity;
import com.bertin.game.store.wishlist.Wishlist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Game extends BaseEntity {
    private String title;
    // PC, XBOX, PS, NINTENDO, ...
    @Enumerated(value = EnumType.STRING)
    private SupportedPlatforms supportedPlatforms;
    private String coverPicture;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "game")
    private List<Comment> comments;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "game_wishlist",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "wishlist_id")
    )
    private List<Wishlist> wishlists;

    public void addWishlist(Wishlist wishlist) {
        this.wishlists.add(wishlist);
        wishlist.getGames().add(this);
    }

    public void removeWishlist(Wishlist wishlist) {
        this.wishlists.remove(wishlist);
        wishlist.getGames().remove(this);
    }
}
