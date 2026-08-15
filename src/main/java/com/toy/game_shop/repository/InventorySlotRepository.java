package com.toy.game_shop.repository;

import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventorySlotRepository extends JpaRepository<InventorySlot, Long> {
    @Query("SELECT s FROM InventorySlot s Join fetch s.item join fetch s.player where s.player.id = :id")
    List<InventorySlot> findByPlayerId(@Param("id") Long id);

    List<InventorySlot> findAllByPlayerAndItem(Player player, Item item);
}
