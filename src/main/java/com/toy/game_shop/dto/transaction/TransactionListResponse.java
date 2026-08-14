package com.toy.game_shop.dto.transaction;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Transaction;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"count","transactions"})
public class TransactionListResponse {
    private final int count;
    private final List<TransactionResponse> transactions;

    public TransactionListResponse(List<Transaction> transactions){
        this.count = transactions.size();
        this.transactions = transactions.stream().map(TransactionResponse::new).toList();
    }
}
