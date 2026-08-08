package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.type.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
    List<Item> findByType(ItemType type);
}
