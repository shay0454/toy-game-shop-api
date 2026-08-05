package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Inventory;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByPlayerAndItem(Player player, Item item);

    List<Inventory> findByPlayer(Player player);
}
