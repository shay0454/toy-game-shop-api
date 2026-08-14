package com.toy.game_shop.dto.item;

import com.toy.game_shop.type.ItemType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateRequest{
    private String name;
    private ItemType type;
    private String description;
}
