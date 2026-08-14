package com.toy.game_shop.dto.inventory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.InventorySlot;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"count","slots"})
public class InventorySlotListResponse {
    private final int count;
    private final List<InventorySlotResponse> slots;

    public InventorySlotListResponse(List<InventorySlot> slots){
        this.count = slots.size();
        this.slots = slots.stream().map(InventorySlotResponse::new).toList();
    }
}
