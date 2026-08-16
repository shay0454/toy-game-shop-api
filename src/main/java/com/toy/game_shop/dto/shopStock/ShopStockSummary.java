package com.toy.game_shop.dto.shopStock;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.type.ItemType;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id","name","type","description","maxStack","stock","price"})
public class ShopStockSummary {
    private Long id;
    private String name;
    private ItemType type;
    private String description;
    private Integer maxStack;
    private Integer stock;
    private Long price;

    public ShopStockSummary(ShopStock stock){
        this.id = stock.getItem().getId();
        this.name = stock.getItem().getName();
        this.type = stock.getItem().getType();
        this.description = stock.getItem().getDescription();
        this.maxStack = stock.getItem().getMaxStack();
        this.stock = stock.getStock();
        this.price = stock.getPrice();
    }
}
