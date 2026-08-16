package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByName(String name);

    @Query("Select s from Shop s join fetch s.stocks st join fetch st.item where s.id = :id")
    Optional<Shop> findByIdWithStocks(@Param("id") Long id);
}
