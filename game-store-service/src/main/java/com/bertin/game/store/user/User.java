package com.bertin.game.store.user;

import com.bertin.game.store.common.BaseEntity;
import com.bertin.game.store.gamerequest.GameRequestEntity;
import com.bertin.game.store.notification.Notification;
import com.bertin.game.store.wishlist.Wishlist;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "_user")
public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePictureUrl;

    @OneToOne(mappedBy = "user")
    private Wishlist wishlist;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<GameRequestEntity> gameRequestEntities;
}
