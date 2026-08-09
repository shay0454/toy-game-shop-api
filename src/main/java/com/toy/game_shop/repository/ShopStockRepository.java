package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopStockRepository extends JpaRepository<ShopStock, Long> {
    Optional<ShopStock> findByShopAndItem(Shop shop, Item item);

    List<ShopStock> findByShop(Shop shop);
}
