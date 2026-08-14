package com.toy.game_shop.dto.shopStock;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopStockPatchRequest {
    private Integer stock;
    private Long price;
}
