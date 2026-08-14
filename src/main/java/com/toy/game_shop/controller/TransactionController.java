package com.toy.game_shop.controller;

import com.toy.game_shop.dto.transaction.TransactionListResponse;
import com.toy.game_shop.dto.transaction.TransactionResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final PlayerService playerService;
    private final ItemService itemService;

    @GetMapping
    public TransactionListResponse findAllTransactions(){
        return new TransactionListResponse(transactionService.findAllTransactions());
    }

    @GetMapping("/{id}")
    public TransactionResponse findTransactionById(@PathVariable Long id){
        return new TransactionResponse(transactionService.findTransactionById(id));
    }

    @GetMapping("/players/{id}")
    public TransactionListResponse findTransactionsByPlayer(@PathVariable Long id){
        Player player = playerService.findById(id);
        return new TransactionListResponse(transactionService.findTransactionsByPlayer(player));
    }

    @GetMapping("/items/{id}")
    public TransactionListResponse findTransactionsByItem(@PathVariable Long id){
        Item item = itemService.findById(id);
        return new TransactionListResponse(transactionService.findTransactionsByItem(item));
    }

    @GetMapping("/players/{playerId}/items/{itemId}")
    public TransactionListResponse findTransactionByPlayerAndItem(
            @PathVariable(name = "playerId") Long playerId,
            @PathVariable(name = "itemId") Long itemId){
        Player player = playerService.findById(playerId);
        Item item = itemService.findById(itemId);

        return new TransactionListResponse(transactionService.findTransactionsByPlayerAndItem(player,item));
    }
}
