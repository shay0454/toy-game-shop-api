package com.toy.game_shop.dto.shop;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopCreateRequest {
    @NotBlank(message = "Shop명 입력")
    private String name;
}
