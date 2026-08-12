package com.toy.game_shop.controller;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Transaction;
import com.toy.game_shop.service.ItemService;
import com.toy.game_shop.service.PlayerService;
import com.toy.game_shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final PlayerService playerService;
    private final ItemService itemService;

    @GetMapping
    public List<Transaction> findAllTransactions(){
        return transactionService.findAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction findTransactionById(@PathVariable Long id){
        return transactionService.findTransactionById(id);
    }

    @GetMapping("/players/{id}")
    public List<Transaction> findTransactionsByPlayer(@PathVariable Long id){
        Player player = playerService.findById(id);
        return transactionService.findTransactionsByPlayer(player);
    }

    @GetMapping("/items/{id}")
    public List<Transaction> findTransactionsByItem(@PathVariable Long id){
        Item item = itemService.findById(id);
        return transactionService.findTransactionsByItem(item);
    }

    @GetMapping("/players/{playerId}/items/{itemId}")
    public List<Transaction> findTransactionByPlayerAndItem(
            @PathVariable(name = "playerId") Long playerId,
            @PathVariable(name = "itemId") Long itemId){
        Player player = playerService.findById(playerId);
        Item item = itemService.findById(itemId);

        return transactionService.findTransactionsByPlayerAndItem(player,item);
    }
}
