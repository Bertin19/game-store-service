package com.bertin.game.store.platform;

import com.bertin.game.store.common.BaseEntity;
import com.bertin.game.store.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Platform extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Console console;
    @ManyToMany(mappedBy = "platforms")
    private List<Game> games;
}
