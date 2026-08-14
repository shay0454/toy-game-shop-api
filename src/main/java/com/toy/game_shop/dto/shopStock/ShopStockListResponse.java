package com.toy.game_shop.dto.shopStock;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.ShopStock;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"count","stocks"})
public class ShopStockListResponse {
    private int count;
    private List<ShopStockResponse> stocks;

    public ShopStockListResponse(List<ShopStock> stock){
        this.count = stock.size();
        this.stocks = stock.stream().map(ShopStockResponse::new).toList();
    }
}
