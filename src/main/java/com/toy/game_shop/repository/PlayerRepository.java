package com.toy.game_shop.repository;


import com.toy.game_shop.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    Optional<Player> findByNickname(String name);
}
