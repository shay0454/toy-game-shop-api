package com.toy.game_shop.dto.inventory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.InventorySlot;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id","itemId","itemName","quantity","position"})
public class InventorySlotEntryResponse {
    private final Long id;
    private final Long itemId;
    private final String itemName;
    private final Integer quantity;
    private final SlotPosition position;

    public InventorySlotEntryResponse(InventorySlot slot){
        this.id = slot.getId();
        this.itemId = slot.getItem().getId();
        this.itemName = slot.getItem().getName();
        this.quantity = slot.getQuantity();
        this.position = new SlotPosition(slot.getRow(), slot.getCol());
    }
}
