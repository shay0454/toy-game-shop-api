package com.toy.game_shop.dto.transaction;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({"id","playerId","itemId","shopId","nickname","itemName","shopName","quantity","priceAtTransaction","transactedTime"})
public class TransactionResponse {
    private final Long id;
    private final Long playerId;
    private final Long itemId;
    private final Long shopId;
    private final String nickname;
    private final String itemName;
    private final String shopName;
    private final Integer quantity;
    private final Long priceAtTransaction;
    private final LocalDateTime transactedTime;

    public TransactionResponse(Transaction transaction){
        this.id = transaction.getId();
        this.playerId = transaction.getPlayer().getId();
        this.itemId = transaction.getItem().getId();
        this.shopId = transaction.getShop().getId();
        this.nickname = transaction.getPlayer().getNickname();
        this.itemName = transaction.getItem().getName();
        this.shopName = transaction.getShop().getName();
        this.quantity = transaction.getQuantity();
        this.priceAtTransaction = transaction.getPriceAtTransaction();
        this.transactedTime = transaction.getTransactedTime();
    }
}
