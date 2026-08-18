package com.toy.game_shop.dto.inventory;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Player;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"playerId","playerNickname","slots"})
public class SlotOrderByPlayerResponse {
    private final Long playerId;
    private final String playerNickname;
    private final int count;
    private final List<InventorySlotEntryResponse> slots;

    public SlotOrderByPlayerResponse(Player player, List<InventorySlot> slots){
        this.playerId = player.getId();
        this.playerNickname = player.getNickname();
        this.count = slots.size();
        this.slots = slots.stream().map(InventorySlotEntryResponse::new).toList();
    }
}
