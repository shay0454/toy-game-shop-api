package com.toy.game_shop.controller;

import com.toy.game_shop.dto.inventory.SlotOrderByPlayerResponse;
import com.toy.game_shop.dto.inventory.InventorySlotResponse;
import com.toy.game_shop.dto.inventory.PositionRequest;
import com.toy.game_shop.dto.inventory.SlotPosition;
import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.service.InventoryService;
import com.toy.game_shop.service.ItemService;
import com.toy.game_shop.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players/{playerId}/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    private final PlayerService playerService;
    private final ItemService itemService;

    @GetMapping
    public SlotOrderByPlayerResponse findByPlayer(@PathVariable Long playerId){
        Player player = playerService.findById(playerId);
        List<InventorySlot> slots = inventoryService.findByPlayerId(playerId);
        return new SlotOrderByPlayerResponse(player, slots);
    }

    @PostMapping("/{itemId}")
    public SlotOrderByPlayerResponse addQuantity(@PathVariable Long playerId, @PathVariable
                                     Long itemId, @RequestParam Integer amount){
        Player player = playerService.findById(playerId);
        Item item = itemService.findById(itemId);
        List<InventorySlot> slots = inventoryService.addQuantity(player,item,amount);
        return new SlotOrderByPlayerResponse(player, slots);
    }

    @PostMapping("/slot/{fromSlotId}/merge/{toSlotId}")
    public ResponseEntity<Void> mergeSlot(@PathVariable Long playerId,
                                          @PathVariable Long fromSlotId,
                                          @PathVariable Long toSlotId){
        inventoryService.mergeSlot(fromSlotId, toSlotId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/slot/{slotId}/split")
    public InventorySlotResponse splitSlot(@PathVariable Long playerId,
                                   @PathVariable Long slotId,
                                   @RequestParam Integer amount){
        return new InventorySlotResponse(inventoryService.splitSlot(slotId, amount));
    }

    @PatchMapping("/slot/{slotId}/position")
    public InventorySlotResponse moveSlot(@PathVariable Long slotId,
                                          @RequestBody @Valid PositionRequest request){
        InventorySlot slot = inventoryService.moveSlot(slotId, new SlotPosition(request.getRow(), request.getCol()));
        return new InventorySlotResponse(slot);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeQuantity(@PathVariable Long playerId, @PathVariable
                                        Long itemId, @RequestParam Integer amount){
        Player player = playerService.findById(playerId);
        Item item = itemService.findById(itemId);
        inventoryService.removeQuantity(player,item,amount);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/slot/{slotId}")
    public ResponseEntity<Void> deleteBySoltId(@PathVariable Long playerId,
                                               @PathVariable Long slotId){
        InventorySlot slot = inventoryService.findBySlotId(slotId);

        if(!slot.getPlayer().getId().equals(playerId)){
            throw new IllegalStateException("본인 소유 슬롯이 아님");
        }

        inventoryService.deleteInventorySlotById(slotId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> deleteAllByItemId(@PathVariable Long playerId,
                                                  @PathVariable Long itemId){
        Player player = playerService.findById(playerId);
        Item item = itemService.findById(itemId);
        inventoryService.deleteByPlayerAndItem(player,item);
        return ResponseEntity.noContent().build();
    }
}
