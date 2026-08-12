package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Transaction;
import com.toy.game_shop.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> findAllTransactions(){
        return transactionRepository.findAll();
    }

    public List<Transaction> findTransactionsByPlayer(Player player){
        return transactionRepository.findByPlayer(player);
    }

    public List<Transaction> findTransactionsByItem(Item item){
        return transactionRepository.findByItem(item);
    }

    public Transaction findTransactionById(Long id){
        return transactionRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Transaction not found"));
    }

    public List<Transaction> findTransactionsByPlayerAndItem(Player player, Item item){
        return transactionRepository.findByPlayerAndItem(player,item);
    }

    public Transaction addTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }
}
