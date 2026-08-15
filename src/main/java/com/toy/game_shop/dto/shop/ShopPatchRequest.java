package com.toy.game_shop.dto.shop;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopPatchRequest {
    @Size(min = 1, message = "Shop명 입력")
    private String name;
}
