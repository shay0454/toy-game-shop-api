package com.toy.game_shop.dto.inventory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.dto.item.ItemResponse;
import com.toy.game_shop.dto.player.PlayerResponse;
import com.toy.game_shop.entity.InventorySlot;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id","player","item","quantity"})
public class InventorySlotResponse {
    private final Long id;
    private final PlayerResponse player;
    private final ItemResponse item;
    private final Integer quantity;

    public InventorySlotResponse(InventorySlot slot){
        this.id = slot.getId();
        this.player = new PlayerResponse(slot.getPlayer());
        this.item = new ItemResponse(slot.getItem());
        this.quantity = slot.getQuantity();
    }
}
