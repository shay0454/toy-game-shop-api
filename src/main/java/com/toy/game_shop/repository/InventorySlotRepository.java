package com.toy.game_shop.repository;

import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventorySlotRepository extends JpaRepository<InventorySlot, Long> {
    Optional<InventorySlot> findByPlayerAndItem(Player player, Item item);

    List<InventorySlot> findByPlayer(Player player);
    List<InventorySlot> findByPlayerId(Long id);
}
