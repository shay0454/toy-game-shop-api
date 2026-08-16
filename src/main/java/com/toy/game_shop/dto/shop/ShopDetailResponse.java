package com.toy.game_shop.dto.shop;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.dto.shopStock.ShopStockSummary;
import com.toy.game_shop.entity.Shop;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"id","name","items"})
public class ShopDetailResponse {
    private Long id;
    private String  name;
    private List<ShopStockSummary> items;

    public ShopDetailResponse(Shop shop){
        this.id = shop.getId();
        this.name = shop.getName();
        this.items = shop.getStocks().stream().map(ShopStockSummary::new).toList();
    }
}
