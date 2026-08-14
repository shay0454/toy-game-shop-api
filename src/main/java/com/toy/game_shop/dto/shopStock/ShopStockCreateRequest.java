package com.toy.game_shop.dto.shopStock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopStockCreateRequest {
    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    @Min(0)
    private Long price;
}
