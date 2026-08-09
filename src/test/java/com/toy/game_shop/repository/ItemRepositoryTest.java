package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.type.ItemType;
import jakarta.websocket.OnMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item sword;

    @BeforeEach
    void before(){
        sword = itemRepository.save(Item.builder().name("Long Sword").price(500L).stock(10).type(ItemType.WEAPON).build());
        itemRepository.save(Item.builder().name("Battle Axe").price(700L).stock(5).type(ItemType.WEAPON).build());
        itemRepository.save(Item.builder().name("Iron Shield").price(300L).stock(8).type(ItemType.ARMOR).build());
        itemRepository.save(Item.builder().name("Health Potion").price(50L).stock(999).type(ItemType.CONSUMABLE).build());
        itemRepository.save(Item.builder().name("Iron Ore").price(20L).stock(500).type(ItemType.MATERIAL).build());
        itemRepository.save(Item.builder().name("Broken Item").price(10L).stock(0).type(ItemType.WEAPON).build());
        itemRepository.save(Item.builder().name("Unknown Box").price(1L).stock(1).type(ItemType.NONE).build());
    }

    @Test
    @DisplayName("Weapon 타입 조회 확인")
    void findWeapon(){
        List<Item> result = itemRepository.findByType(ItemType.WEAPON);

        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result).allMatch(i->i.getType() == ItemType.WEAPON);
    }

    @Test
    @DisplayName("None 타입 조회 확인")
    void findNone(){
        List<Item> result = itemRepository.findByType(ItemType.NONE);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result).allMatch(i->i.getType() == ItemType.NONE);
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findItemById(){
        Optional<Item> item = itemRepository.findById(sword.getId());

        Assertions.assertThat(item).isPresent();
        Assertions.assertThat(item.get().getName()).isEqualTo("Long Sword");
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void findItemByNonId(){
        Optional<Item> item = itemRepository.findById(-1L);
        Assertions.assertThat(item).isEmpty();
    }



    @Test
    @DisplayName("이름으로 조회 확인")
    void findItemByName(){
        Optional<Item> item = itemRepository.findByName("Long Sword");
        Assertions.assertThat(item).isPresent();
        Assertions.assertThat(item.get().getName()).isEqualTo("Long Sword");
    }

    @Test
    @DisplayName("미존재 이름 조회 확인")
    void findItemByNonName(){
        Optional<Item> item = itemRepository.findByName("");
        Assertions.assertThat(item).isEmpty();
    }

    @Test
    @DisplayName("삭제 확인")
    void deleteItemById(){
        itemRepository.deleteById(sword.getId());

        Optional<Item> item = itemRepository.findById(sword.getId());
        Assertions.assertThat(item).isEmpty();
    }
}