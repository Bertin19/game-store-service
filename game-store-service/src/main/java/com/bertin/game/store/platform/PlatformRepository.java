package com.bertin.game.store.platform;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PlatformRepository extends JpaRepository<Platform, String> {
    @Query("""
           SELECT platform FROM Platform platform
           WHERE platform.console IN :consoles
           """)
    Set<Platform> findAllConsoleIn(@Param("consoles") Set<Console> selectedConsoles);
}
