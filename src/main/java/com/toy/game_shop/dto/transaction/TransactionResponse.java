package com.toy.game_shop.dto.transaction;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.dto.item.ItemResponse;
import com.toy.game_shop.dto.player.PlayerResponse;
import com.toy.game_shop.dto.shop.ShopResponse;
import com.toy.game_shop.entity.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"id","player","item","shop","quantity","priceAtTransaction","transactedTime"})
public class TransactionResponse {
    private final Long id;
    private final PlayerResponse player;
    private final ItemResponse item;
    private final ShopResponse shop;
    private final Integer quantity;
    private final Long priceAtTransaction;
    private final LocalDateTime transactedTime;

    public TransactionResponse(Transaction transaction){
        this.id = transaction.getId();
        this.player = new PlayerResponse(transaction.getPlayer());
        this.item = new ItemResponse(transaction.getItem());
        this.shop = new ShopResponse(transaction.getShop());
        this.quantity = transaction.getQuantity();
        this.priceAtTransaction = transaction.getPriceAtTransaction();
        this.transactedTime = transaction.getTransactedTime();
    }
}
