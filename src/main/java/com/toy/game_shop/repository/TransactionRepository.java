package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPlayer(Player player);
}
