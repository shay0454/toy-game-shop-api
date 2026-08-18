package com.toy.game_shop.dto.shopStock;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.ShopStock;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id","shopId","itemId","shopName","itemName","stock","price"})
public class ShopStockResponse {
    private final Long id;
    private final Long shopId;
    private final Long itemId;
    private final String shopName;
    private final String itemName;
    private final Integer stock;
    private final Long price;

    public ShopStockResponse(ShopStock stock){
        this.id = stock.getId();
        this.shopId = stock.getShop().getId();
        this.itemId = stock.getItem().getId();
        this.shopName = stock.getShop().getName();
        this.itemName = stock.getItem().getName();
        this.stock = stock.getStock();
        this.price = stock.getPrice();
    }
}
