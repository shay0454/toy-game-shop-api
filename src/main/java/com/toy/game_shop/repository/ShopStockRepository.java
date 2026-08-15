package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShopStockRepository extends JpaRepository<ShopStock, Long> {
    Optional<ShopStock> findByShopAndItem(Shop shop, Item item);

    @Query("SELECT s FROM ShopStock s JOIN FETCH s.shop JOIN FETCH s.item WHERE s.shop.id = :shopId")
    List<ShopStock> findByShopId(@Param("shopId") Long shopId);
}
