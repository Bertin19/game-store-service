package com.bertin.game.store.notification;

import com.bertin.game.store.common.BaseEntity;
import com.bertin.game.store.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
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
public class Notification extends BaseEntity {
    private String message;
    private String receiver;
    @Enumerated(EnumType.STRING)
    private NotificationLevel level;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
