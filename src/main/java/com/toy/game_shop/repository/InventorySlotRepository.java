package com.toy.game_shop.repository;

import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventorySlotRepository extends JpaRepository<InventorySlot, Long> {
    List<InventorySlot> findByPlayerId(Long id);

    List<InventorySlot> findAllByPlayerAndItem(Player player, Item item);
}
