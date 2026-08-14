package com.toy.game_shop.dto.item;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Item;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"count","items"})
public class ItemListResponse {
    private final int count;
    private final List<ItemResponse> items;

    public ItemListResponse(List<Item> items){
        this.count = items.size();
        this.items = items.stream().map(ItemResponse::new).toList();
    }
}
