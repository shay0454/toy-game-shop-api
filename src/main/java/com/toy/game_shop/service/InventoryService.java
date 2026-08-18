package com.toy.game_shop.service;

import com.toy.game_shop.dto.inventory.SlotPosition;
import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.repository.InventorySlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventorySlotRepository inventorySlotRepository;

    // TODO: 임의로 설정한 값, 추후 조정 검토
    private static final int MAX_SLOTS = 60;

    private InventorySlot newSlot(Player player, Item item){
        return InventorySlot.builder()
                .player(player)
                .item(item)
                .quantity(0)
                .build();
    }

    public InventorySlot findBySlotId(Long slotId){
        return inventorySlotRepository.findById(slotId)
            .orElseThrow(()-> new NoSuchElementException("Slot not found"));
    }

    public List<InventorySlot> findByPlayerId(Long id){
        return inventorySlotRepository.findByPlayerId(id);
    }

    public void deleteInventorySlotById(Long id){
        inventorySlotRepository.deleteById(id);
    }

    @Transactional
    public List<InventorySlot> createSlots(Player player, Item item, Integer quantity){
        if(quantity <= 0){
            throw new IllegalArgumentException("quantity는 0보다 커야 함");
        }

        BitSet occupied = buildOccupiedBitSet(player.getId());
        List<InventorySlot> created = new ArrayList<>();
        int remaining = quantity;

        while (remaining > 0){
            int chunk = Math.min(remaining, item.getMaxStack());
            InventorySlot slot = newSlot(player,item);
            slot.setQuantity(chunk);

            SlotPosition position = findAvailablePosition(occupied,null);
            slot.setRow(position.row());
            slot.setCol(position.col());

            created.add(slot);
            remaining -= chunk;
        }

        return inventorySlotRepository.saveAll(created);
    }

    @Transactional
    public List<InventorySlot> addQuantity(Player player, Item item, Integer amount){
        if(amount <= 0){
            throw new IllegalArgumentException("amount는 0보다 커야 함");
        }

        List<InventorySlot> slots = inventorySlotRepository.findAllByPlayerAndItem(player, item);

        // 해당 아이템이 이미 인벤에 있는 아이템인지 탐색
        Optional<InventorySlot> target = slots.stream()
                .filter(slot -> slot.getQuantity() < item.getMaxStack())
                .findFirst();

        // 아이템이 없거나, 최대 수량 상태로만 있으면 추가
        if(target.isEmpty()){
            return createSlots(player, item, amount);
        }

        // 있으면 최대 수량전까지는 해당 아이템에 채우고 그 외는 칸에 아이템 추가
        InventorySlot inventorySlot = target.get();
        int total = inventorySlot.getQuantity() + amount;

        if(total > item.getMaxStack()){
            int overflow = total - item.getMaxStack();
            inventorySlot.setQuantity(item.getMaxStack());
            inventorySlotRepository.save(inventorySlot);

            List<InventorySlot> result = new ArrayList<>();
            result.add(inventorySlot);
            result.addAll(createSlots(player, item, overflow));
            return result;
        }

        inventorySlot.setQuantity(total);
        return List.of(inventorySlotRepository.save(inventorySlot));
    }

    @Transactional
    public void removeQuantity(Player player, Item item, Integer amount){
        if(amount <= 0){
            throw new IllegalArgumentException("amount는 0보다 커야 함");
        }

        List<InventorySlot> slots = inventorySlotRepository
                .findAllByPlayerAndItem(player,item);

        int totalAvailable = slots.stream().mapToInt(InventorySlot::getQuantity).sum();
        if(amount > totalAvailable){
            throw new IllegalStateException("수량 부족");
        }

        List<InventorySlot> affected = new ArrayList<>();
        List<InventorySlot> toDelete = new ArrayList<>();

        int remaining = amount;

        for(InventorySlot slot : slots){
            if(remaining <= 0 ) break;

            // 빼낼 양 계산 및 남은 양 계산
            int deduct = Math.min(slot.getQuantity(),remaining);
            int left = slot.getQuantity()-deduct;
            remaining -= deduct;

            // 모두 빼낼 경우 toDelete로, 아닐 경우 affected로 넣어
            if(left == 0){
                toDelete.add(slot);
            }else{
                slot.setQuantity(left);
                affected.add(slot);
            }
        }
        // 제거해야 될 자원은 제거하고 양만 수정 해야될 자원은 수정만 실행
        inventorySlotRepository.deleteAll(toDelete);
        inventorySlotRepository.saveAll(affected);
    }

    @Transactional
    public InventorySlot moveSlot(Long slotId, SlotPosition target){
        InventorySlot slot = findBySlotId(slotId);

        if(target != null && target.row() == slot.getRow() && target.col() == slot.getCol()){
            throw new IllegalArgumentException("이미 해당 위치임");
        }

        // 해당 위치가 안 될 경우 예외 발생
        BitSet occupied = buildOccupiedBitSet(slot.getPlayer().getId());
        occupied.clear((slot.getRow()-1)*COLS + (slot.getCol()-1));
        SlotPosition validated = findAvailablePosition(occupied, target);

        slot.setRow(validated.row());
        slot.setCol(validated.col());

        return inventorySlotRepository.save(slot);
    }

    @Transactional
    public void mergeSlot(Long fromSlotId, Long toSlotId){
        InventorySlot from = findBySlotId(fromSlotId);
        InventorySlot to = findBySlotId(toSlotId);

        // 같은 슬롯일 경우
        if(fromSlotId.equals(toSlotId)){
            throw new IllegalStateException("같은 슬롯끼리 합칠 수 없음");
        }

        // 권한이 없을 경우
        if(!from.getPlayer().getId().equals(to.getPlayer().getId())){
            throw new IllegalStateException("다른 플레이어 소유 아이템과는 합칠 수 없음");
        }

        // 아이템이 다른 경우
        if(!from.getItem().getId().equals(to.getItem().getId())){
            throw new IllegalStateException("다른 아이템끼리 합칠 수 없음");
        }

        int total = to.getQuantity() + from.getQuantity();
        int merged = Math.min(total,from.getItem().getMaxStack());
        int leftover = total - merged;

        to.setQuantity(merged);
        inventorySlotRepository.save(to);

        if(leftover== 0){
            inventorySlotRepository.delete(from);
        } else {
            from.setQuantity(leftover);
            inventorySlotRepository.save(from);
        }
    }

    @Transactional
    public InventorySlot splitSlot(Long slotId, Integer amount){
        InventorySlot original = findBySlotId(slotId);

        if(amount <= 0 || amount >= original.getQuantity()){
            throw new IllegalArgumentException("분할 수량이 올바르지 않음");
        }

        original.setQuantity(original.getQuantity() - amount);
        inventorySlotRepository.save(original);

        return createSlots(original.getPlayer(), original.getItem(), amount).get(0);
    }

    public void deleteByPlayerAndItem(Player player, Item item){
        List<InventorySlot> slots
                = inventorySlotRepository.findAllByPlayerAndItem(player,item);

        inventorySlotRepository.deleteAll(slots);
    }

    private static final int ROWS = 6;
    private static final int COLS = 10;

    private SlotPosition findAvailablePosition(BitSet occupied, SlotPosition position){
        // row & col 지정 시, 해당 칸이 비었는지만 확인
        if(position != null){
            int index = (position.row()-1)*COLS + (position.col()-1);
            if(occupied.get(index)){
                throw new IllegalStateException("해당 칸에 이미 아이템이 존재");
            }
            occupied.set(index);
            return position;
        }

        int index = occupied.nextClearBit(0);
        if(index>= ROWS*COLS){
            throw new IllegalStateException("인벤에 빈 칸이 없음");
        }
        occupied.set(index);
        return new SlotPosition(index/COLS+1, index%COLS+1);
    }

    private BitSet buildOccupiedBitSet(Long playerId){
        BitSet occupied = new BitSet(ROWS*COLS);
        inventorySlotRepository.findByPlayerId(playerId)
                .forEach(slot ->
                        occupied.set((slot.getRow()-1)*COLS+(slot.getCol()-1)));

        return occupied;
    }
}
