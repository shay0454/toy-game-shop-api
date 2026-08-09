package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.patchRequest.ItemPatchRequest;
import com.toy.game_shop.type.ItemType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ItemService.class)
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    private Item newItem(String name,ItemType type){
        return Item.builder()
                .name(name)
                .price(500L)
                .stock(10)
                .type(type)
                .build();
    }

    @Test
    @DisplayName("생성 확인")
    void addItem(){
        Item item = newItem("Sword",ItemType.WEAPON);

        Item saved = itemService.addItem(item);

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo("Sword");
        Assertions.assertThat(saved.getPrice()).isEqualTo(500L);
        Assertions.assertThat(saved.getStock()).isEqualTo(10);
        Assertions.assertThat(saved.getType()).isEqualTo(ItemType.WEAPON);
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findById(){
        Item item = newItem("Sword",ItemType.WEAPON);

        Item saved = itemService.addItem(item);
        Item found = itemService.findById(saved.getId());

        Assertions.assertThat(found.getId()).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo("Sword");
        Assertions.assertThat(found.getPrice()).isEqualTo(500L);
        Assertions.assertThat(found.getStock()).isEqualTo(10);
        Assertions.assertThat(found.getType()).isEqualTo(ItemType.WEAPON);
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void findByNonId(){
        Assertions.assertThatThrownBy(()->itemService.findById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("이름 조회 확인")
    void findByName(){
        Item item = newItem("Sword",ItemType.WEAPON);
        itemService.addItem(item);

        Item found = itemService.findByName("Sword");

        Assertions.assertThat(found.getId()).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo("Sword");
        Assertions.assertThat(found.getPrice()).isEqualTo(500L);
        Assertions.assertThat(found.getStock()).isEqualTo(10);
        Assertions.assertThat(found.getType()).isEqualTo(ItemType.WEAPON);
    }

    @Test
    @DisplayName("미존재 이름 조회 확인")
    void findByNonName(){
        Assertions.assertThatThrownBy(()->itemService.findByName("Long"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("모든 item 조회 확인")
    void findAll(){
        itemService.addItem(newItem("Sword",ItemType.WEAPON));
        itemService.addItem(newItem("Armor",ItemType.ARMOR));
        itemService.addItem(newItem("trash",null));

        Assertions.assertThat(itemService.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("타입으로 조회 확인")
    void findItemsByType(){
        itemService.addItem(newItem("Sword",ItemType.WEAPON));
        itemService.addItem(newItem("Armor",ItemType.ARMOR));
        itemService.addItem(newItem("Sax",ItemType.WEAPON));

        Assertions.assertThat(itemService.findByType(ItemType.WEAPON)
                .size()).isEqualTo(2);
    }

    @Test
    @DisplayName("타입 설정 없을 시, None 타입 확인")
    void addItemTypeNull(){
        Item item = newItem("Sword",null);

        Item saved = itemService.addItem(item);
        Assertions.assertThat(saved.getType()).isEqualTo(ItemType.NONE);
    }

    @Test
    @DisplayName("패치 적용 확인")
    void updateItemById(){
        Item item = newItem("Sword",ItemType.WEAPON);

        ItemPatchRequest request = new ItemPatchRequest();
        request.setPrice(2000L);
        request.setStock(1000);

        Item saved = itemService.addItem(item);
        Item updated = itemService.updateItem(saved.getId(),request);

        Assertions.assertThat(updated.getName()).isEqualTo("Sword");
        Assertions.assertThat(updated.getPrice()).isEqualTo(2000L);
        Assertions.assertThat(updated.getStock()).isEqualTo(1000);
        Assertions.assertThat(updated.getType()).isEqualTo(ItemType.WEAPON);
    }

    @Test
    @DisplayName("미존재 id 패치 적용 확인")
    void updateItemByNonId(){
        ItemPatchRequest request = new ItemPatchRequest();
        request.setPrice(2000L);

        Assertions.assertThatThrownBy(()->itemService.updateItem(-1L,request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("제거 확인")
    void deleteItemById(){
        Item item = newItem("Sword",ItemType.WEAPON);

        Item saved = itemService.addItem(item);
        Item found = itemService.findById(saved.getId());
        itemService.deleteItemById(found.getId());

        Assertions.assertThatThrownBy(()->itemService.findById(found.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}