package com.bertin.game.store.wishlist;

import com.bertin.game.store.common.BaseEntity;
import com.bertin.game.store.game.Game;
import com.bertin.game.store.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
public class Wishlist extends BaseEntity {
    private String name;

    @OneToOne
    private User user;

    @ManyToMany(mappedBy = "wishlists", fetch = FetchType.EAGER)
    private List<Game> games;
}
