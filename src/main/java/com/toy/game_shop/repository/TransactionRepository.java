package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t JOIN FETCH t.player JOIN FETCH t.item JOIN FETCH t.shop")
    List<Transaction> findAllWithDetails();

    @Query("SELECT t FROM Transaction t JOIN FETCH t.player JOIN FETCH t.item JOIN FETCH t.shop WHERE t.player = :player")
    List<Transaction> findByPlayer(@Param("player") Player player);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.player JOIN FETCH t.item JOIN FETCH t.shop WHERE t.item = :item")
    List<Transaction> findByItem(@Param("item") Item item);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.player JOIN FETCH t.item JOIN FETCH t.shop WHERE t.player = :player AND t.item = :item")
    List<Transaction> findByPlayerAndItem(@Param("player") Player player, @Param("item") Item item);
}
