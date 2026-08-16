package com.toy.game_shop.dto.item;

import com.toy.game_shop.type.ItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateRequest{
    @NotBlank(message = "Item명 입력")
    private String name;

    private ItemType type;

    @Size(max = 500)
    private String description;

    @Min(1)
    private Integer maxStack;
}
