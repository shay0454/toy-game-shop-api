package com.toy.game_shop.dto.shop;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Shop;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"count","shops"})
public class ShopListResponse{
    private int count;
    private List<ShopResponse> shops;

    public ShopListResponse(List<Shop> shops){
        this.count = shops.size();
        this.shops = shops.stream().map(ShopResponse::new).toList();
    }
}
