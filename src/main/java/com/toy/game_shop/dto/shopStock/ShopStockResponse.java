package com.toy.game_shop.dto.shopStock;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.dto.item.ItemResponse;
import com.toy.game_shop.dto.shop.ShopResponse;
import com.toy.game_shop.entity.ShopStock;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id","shop","item","stock","price"})
public class ShopStockResponse {
    private Long id;
    private ShopResponse shop;
    private ItemResponse item;
    private Integer stock;
    private Long price;

    public ShopStockResponse(ShopStock stock){
        this.id = stock.getId();
        this.shop = new ShopResponse(stock.getShop());
        this.item = new ItemResponse(stock.getItem());
        this.stock = stock.getStock();
        this.price = stock.getPrice();
    }
}
