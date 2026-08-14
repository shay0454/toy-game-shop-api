package com.toy.game_shop.dto.purchase;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseRequest {
    private Long shopId;
    private Long itemId;
    private Integer quantity;
}
