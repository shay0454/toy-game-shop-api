package com.toy.game_shop.dto.shop;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Shop;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"id","name"})
public class ShopResponse {
    private Long id;
    private String name;

    public ShopResponse(Shop shop){
        this.id = shop.getId();
        this.name = shop.getName();
    }
}

