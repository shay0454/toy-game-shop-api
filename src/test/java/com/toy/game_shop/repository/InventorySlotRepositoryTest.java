package com.toy.game_shop.repository;

import com.toy.game_shop.entity.InventorySlot;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.type.ItemType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventorySlotRepositoryTest {

    @Autowired
    private InventorySlotRepository inventorySlotRepository;

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

        inventorySlotRepository.save(InventorySlot.builder().player(player).item(item).quantity(50).build());
    }

    @Test
    @DisplayName("playerId로 전체 슬롯 조회 확인")
    void findByPlayerId(){
        Item otherItem = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());
        inventorySlotRepository.save(InventorySlot.builder().player(player).item(otherItem).quantity(1).build());

        List<InventorySlot> slots = inventorySlotRepository.findByPlayerId(player.getId());

        Assertions.assertThat(slots.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("같은 player, item으로 여러 슬롯 조회 확인")
    void findAllByPlayerAndItem(){
        inventorySlotRepository.save(InventorySlot.builder().player(player).item(item).quantity(30).build());

        List<InventorySlot> slots = inventorySlotRepository.findAllByPlayerAndItem(player, item);

        Assertions.assertThat(slots.size()).isEqualTo(2);
        Assertions.assertThat(slots).extracting(InventorySlot::getQuantity)
                .containsExactlyInAnyOrder(50, 30);
    }
}
