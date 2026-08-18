package com.toy.game_shop.service;

import com.toy.game_shop.dto.inventory.SlotPosition;
import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.repository.ItemRepository;
import com.toy.game_shop.repository.PlayerRepository;
import com.toy.game_shop.type.ItemType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(InventoryService.class)
class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Player player;
    private Item item;

    @BeforeEach
    void before(){
        player = playerRepository.save(Player.builder().nickname("tester").gold(0L).build());
        item = itemRepository.save(Item.builder().name("Potion").type(ItemType.CONSUMABLE).build());
    }

    @Test
    @DisplayName("슬롯 id 조회 확인")
    void findBySlotId(){
        InventorySlot slot = inventoryService.createSlots(player, item, 10).get(0);

        InventorySlot found = inventoryService.findBySlotId(slot.getId());

        Assertions.assertThat(found.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("미존재 슬롯 id 조회 확인")
    void findBySlotIdNonId(){
        Assertions.assertThatThrownBy(()->inventoryService.findBySlotId(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("플레이어로 전체 슬롯 조회 확인")
    void findByPlayerId(){
        inventoryService.createSlots(player, item, 10);

        Item item2 = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());
        inventoryService.createSlots(player, item2, 1);

        List<InventorySlot> slots = inventoryService.findByPlayerId(player.getId());

        Assertions.assertThat(slots.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("슬롯 삭제 확인")
    void deleteInventorySlotById(){
        InventorySlot slot = inventoryService.createSlots(player, item, 10).get(0);

        inventoryService.deleteInventorySlotById(slot.getId());

        Assertions.assertThatThrownBy(()->inventoryService.findBySlotId(slot.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("99 이하 수량 생성 시 슬롯 1개 확인")
    void createSlotsUnderMax(){
        List<InventorySlot> slots = inventoryService.createSlots(player, item, 50);

        Assertions.assertThat(slots.size()).isEqualTo(1);
        Assertions.assertThat(slots.get(0).getQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("99 초과 수량 생성 시 슬롯 분할 확인")
    void createSlotsOverMax(){
        List<InventorySlot> slots = inventoryService.createSlots(player, item, 120);

        Assertions.assertThat(slots.size()).isEqualTo(2);
        Assertions.assertThat(slots).extracting(InventorySlot::getQuantity)
                .containsExactlyInAnyOrder(99, 21);
    }

    @Test
    @DisplayName("아이템별 maxStack 기준으로 슬롯 분할 확인")
    void createSlotsRespectsItemMaxStack(){
        Item stackable5 = itemRepository.save(Item.builder().name("Arrow").type(ItemType.CONSUMABLE).maxStack(5).build());

        List<InventorySlot> slots = inventoryService.createSlots(player, stackable5, 12);

        Assertions.assertThat(slots.size()).isEqualTo(3);
        Assertions.assertThat(slots).extracting(InventorySlot::getQuantity)
                .containsExactlyInAnyOrder(5, 5, 2);
    }

    @Test
    @DisplayName("생성 수량 0 이하 시 예외 확인")
    void createSlotsNonPositive(){
        Assertions.assertThatThrownBy(()->inventoryService.createSlots(player, item, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("슬롯 60개 제한 초과 시 예외 확인")
    void createSlotsOverSlotLimit(){
        for(int i=0;i<60;i++){
            inventoryService.createSlots(player, item, 1);
        }

        Assertions.assertThatThrownBy(()->inventoryService.createSlots(player, item, 1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("슬롯 없을 때 addQuantity 시 새로 생성 확인")
    void addQuantityCreatesNew(){
        List<InventorySlot> result = inventoryService.addQuantity(player, item, 10);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("자리 있는 기존 슬롯에 채워짐 확인")
    void addQuantityFillsExisting(){
        inventoryService.createSlots(player, item, 10);

        List<InventorySlot> result = inventoryService.addQuantity(player, item, 5);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getQuantity()).isEqualTo(15);
    }

    @Test
    @DisplayName("기존 슬롯 채우다 초과 시 새 슬롯 추가 생성 확인")
    void addQuantityOverflowsToNewSlot(){
        inventoryService.createSlots(player, item, 90);

        List<InventorySlot> result = inventoryService.addQuantity(player, item, 20);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).extracting(InventorySlot::getQuantity)
                .containsExactlyInAnyOrder(99, 11);
    }

    @Test
    @DisplayName("기존 슬롯 전부 꽉 찼을 때 새 슬롯 생성 확인")
    void addQuantityAllFull(){
        inventoryService.createSlots(player, item, 99);

        List<InventorySlot> result = inventoryService.addQuantity(player, item, 5);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("아이템별 maxStack 기준으로 오버플로우 확인")
    void addQuantityRespectsItemMaxStack(){
        Item stackable5 = itemRepository.save(Item.builder().name("Arrow").type(ItemType.CONSUMABLE).maxStack(5).build());
        inventoryService.createSlots(player, stackable5, 3);

        List<InventorySlot> result = inventoryService.addQuantity(player, stackable5, 4);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).extracting(InventorySlot::getQuantity)
                .containsExactlyInAnyOrder(5, 2);
    }

    @Test
    @DisplayName("추가 수량 0 이하 시 예외 확인")
    void addQuantityNonPositive(){
        Assertions.assertThatThrownBy(()->inventoryService.addQuantity(player, item, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("슬롯 하나에서 정상 차감 확인")
    void removeQuantityFromSingleSlot(){
        inventoryService.createSlots(player, item, 50);

        inventoryService.removeQuantity(player, item, 20);

        List<InventorySlot> slots = inventoryService.findByPlayerId(player.getId());
        Assertions.assertThat(slots.get(0).getQuantity()).isEqualTo(30);
    }

    @Test
    @DisplayName("여러 슬롯에 걸쳐 차감 확인")
    void removeQuantityAcrossSlots(){
        inventoryService.createSlots(player, item, 120);

        inventoryService.removeQuantity(player, item, 100);

        List<InventorySlot> slots = inventoryService.findByPlayerId(player.getId());
        int totalLeft = slots.stream().mapToInt(InventorySlot::getQuantity).sum();
        Assertions.assertThat(totalLeft).isEqualTo(20);
    }

    @Test
    @DisplayName("정확히 다 빼면 슬롯 삭제 확인")
    void removeQuantityDeletesEmptySlot(){
        inventoryService.createSlots(player, item, 30);

        inventoryService.removeQuantity(player, item, 30);

        List<InventorySlot> slots = inventoryService.findByPlayerId(player.getId());
        Assertions.assertThat(slots).isEmpty();
    }

    @Test
    @DisplayName("보유량보다 많이 빼려 할 때 예외 확인")
    void removeQuantityInsufficient(){
        inventoryService.createSlots(player, item, 10);

        Assertions.assertThatThrownBy(()->inventoryService.removeQuantity(player, item, 20))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("차감 수량 0 이하 시 예외 확인")
    void removeQuantityNonPositive(){
        Assertions.assertThatThrownBy(()->inventoryService.removeQuantity(player, item, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("정상 머지 확인")
    void mergeSlotNormal(){
        InventorySlot to = inventoryService.createSlots(player, item, 50).get(0);
        InventorySlot from = inventoryService.createSlots(player, item, 30).get(0);

        inventoryService.mergeSlot(from.getId(), to.getId());

        InventorySlot updatedTo = inventoryService.findBySlotId(to.getId());
        Assertions.assertThat(updatedTo.getQuantity()).isEqualTo(80);
        Assertions.assertThatThrownBy(()->inventoryService.findBySlotId(from.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("머지 시 99 초과하면 to는 99로 캡, from엔 나머지 남음 확인")
    void mergeSlotOverMax(){
        InventorySlot to = inventoryService.createSlots(player, item, 90).get(0);
        InventorySlot from = inventoryService.createSlots(player, item, 20).get(0);

        inventoryService.mergeSlot(from.getId(), to.getId());

        InventorySlot updatedTo = inventoryService.findBySlotId(to.getId());
        InventorySlot updatedFrom = inventoryService.findBySlotId(from.getId());

        Assertions.assertThat(updatedTo.getQuantity()).isEqualTo(99);
        Assertions.assertThat(updatedFrom.getQuantity()).isEqualTo(11);
    }

    @Test
    @DisplayName("머지 시 아이템별 maxStack 기준으로 캡 확인")
    void mergeSlotRespectsItemMaxStack(){
        Item stackable5 = itemRepository.save(Item.builder().name("Arrow").type(ItemType.CONSUMABLE).maxStack(5).build());
        InventorySlot to = inventoryService.createSlots(player, stackable5, 4).get(0);
        InventorySlot from = inventoryService.createSlots(player, stackable5, 3).get(0);

        inventoryService.mergeSlot(from.getId(), to.getId());

        InventorySlot updatedTo = inventoryService.findBySlotId(to.getId());
        InventorySlot updatedFrom = inventoryService.findBySlotId(from.getId());

        Assertions.assertThat(updatedTo.getQuantity()).isEqualTo(5);
        Assertions.assertThat(updatedFrom.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("같은 슬롯끼리 머지 시 예외 확인")
    void mergeSlotSameSlot(){
        InventorySlot slot = inventoryService.createSlots(player, item, 50).get(0);

        Assertions.assertThatThrownBy(()->inventoryService.mergeSlot(slot.getId(), slot.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("다른 플레이어 소유 슬롯끼리 머지 시 예외 확인")
    void mergeSlotDifferentPlayer(){
        InventorySlot to = inventoryService.createSlots(player, item, 50).get(0);

        Player otherPlayer = playerRepository.save(Player.builder().nickname("other").gold(0L).build());
        InventorySlot from = inventoryService.createSlots(otherPlayer, item, 20).get(0);

        Assertions.assertThatThrownBy(()->inventoryService.mergeSlot(from.getId(), to.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("다른 아이템끼리 머지 시 예외 확인")
    void mergeSlotDifferentItem(){
        InventorySlot to = inventoryService.createSlots(player, item, 50).get(0);

        Item otherItem = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());
        InventorySlot from = inventoryService.createSlots(player, otherItem, 20).get(0);

        Assertions.assertThatThrownBy(()->inventoryService.mergeSlot(from.getId(), to.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("정상 분할 확인")
    void splitSlotNormal(){
        InventorySlot original = inventoryService.createSlots(player, item, 50).get(0);

        InventorySlot split = inventoryService.splitSlot(original.getId(), 20);

        InventorySlot updatedOriginal = inventoryService.findBySlotId(original.getId());
        Assertions.assertThat(updatedOriginal.getQuantity()).isEqualTo(30);
        Assertions.assertThat(split.getQuantity()).isEqualTo(20);
        Assertions.assertThat(split.getId()).isNotEqualTo(original.getId());
    }

    @Test
    @DisplayName("분할 수량 0 이하 시 예외 확인")
    void splitSlotNonPositive(){
        InventorySlot original = inventoryService.createSlots(player, item, 50).get(0);

        Assertions.assertThatThrownBy(()->inventoryService.splitSlot(original.getId(), 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("분할 수량이 원본 수량 이상일 때 예외 확인")
    void splitSlotTooMuch(){
        InventorySlot original = inventoryService.createSlots(player, item, 50).get(0);

        Assertions.assertThatThrownBy(()->inventoryService.splitSlot(original.getId(), 50))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("player+item으로 슬롯 전부 삭제 확인")
    void deleteByPlayerAndItem(){
        inventoryService.createSlots(player, item, 50);
        inventoryService.createSlots(player, item, 30);

        inventoryService.deleteByPlayerAndItem(player, item);

        List<InventorySlot> slots = inventoryService.findByPlayerId(player.getId());
        Assertions.assertThat(slots).isEmpty();
    }

    @Test
    @DisplayName("빈 칸으로 위치 이동 확인")
    void moveSlotNormal(){
        InventorySlot slot = inventoryService.createSlots(player, item, 10).get(0);

        InventorySlot moved = inventoryService.moveSlot(slot.getId(), new SlotPosition(2, 3));

        Assertions.assertThat(moved.getRow()).isEqualTo(2);
        Assertions.assertThat(moved.getCol()).isEqualTo(3);
    }

    @Test
    @DisplayName("자기 자신의 현재 위치로 이동 시 예외 확인")
    void moveSlotSamePosition(){
        InventorySlot slot = inventoryService.createSlots(player, item, 10).get(0);

        Assertions.assertThatThrownBy(()->inventoryService.moveSlot(
                        slot.getId(), new SlotPosition(slot.getRow(), slot.getCol())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("미존재 슬롯 위치 이동 시 예외 확인")
    void moveSlotNonId(){
        Assertions.assertThatThrownBy(()->inventoryService.moveSlot(-1L, new SlotPosition(2, 3)))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("다른 슬롯이 이미 점유한 위치로 이동 시 예외 확인")
    void moveSlotOccupiedByOtherSlot(){
        InventorySlot slot1 = inventoryService.createSlots(player, item, 10).get(0);

        Item item2 = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());
        InventorySlot slot2 = inventoryService.createSlots(player, item2, 5).get(0);

        Assertions.assertThatThrownBy(()->inventoryService.moveSlot(
                        slot1.getId(), new SlotPosition(slot2.getRow(), slot2.getCol())))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("다른 플레이어가 점유한 위치는 이동에 영향 없음 확인")
    void moveSlotIgnoresOtherPlayerPositions(){
        InventorySlot mySlot = inventoryService.createSlots(player, item, 10).get(0);

        Player other = playerRepository.save(Player.builder().nickname("other2").gold(0L).build());
        InventorySlot otherSlot = inventoryService.createSlots(other, item, 5).get(0);
        InventorySlot movedOtherSlot = inventoryService.moveSlot(otherSlot.getId(), new SlotPosition(4, 4));

        InventorySlot moved = inventoryService.moveSlot(
                mySlot.getId(), new SlotPosition(movedOtherSlot.getRow(), movedOtherSlot.getCol()));

        Assertions.assertThat(moved.getRow()).isEqualTo(4);
        Assertions.assertThat(moved.getCol()).isEqualTo(4);
    }
}
