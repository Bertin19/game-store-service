package com.bertin.game.store.gamerequest;

import com.bertin.game.store.common.BaseEntity;
import com.bertin.game.store.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameRequest extends BaseEntity {
    private String title;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    private User user;
}
