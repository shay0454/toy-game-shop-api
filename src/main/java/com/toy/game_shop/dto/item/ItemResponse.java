package com.toy.game_shop.dto.item;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.type.ItemType;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id","name","type","description"})
public class ItemResponse {
    private final Long id;
    private final String name;
    private final ItemType type;
    private final String description;

    public ItemResponse(Item item){
        this.id = item.getId();
        this.name = item.getName();
        this.type = item.getType();
        this.description = item.getDescription();
    }
}
