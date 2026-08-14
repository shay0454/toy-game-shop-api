package com.toy.game_shop.service;

import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.dto.shop.ShopPatchRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ShopService.class)
class ShopServiceTest {

    @Autowired
    private ShopService shopService;

    private Shop newShop(String name){
        return Shop.builder().name(name).build();
    }

    @Test
    @DisplayName("생성 확인")
    void addShop(){
        Shop saved = shopService.addShop(newShop("shop1"));

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getName()).isEqualTo("shop1");
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findById(){
        Shop saved = shopService.addShop(newShop("shop1"));

        Shop found = shopService.findById(saved.getId());

        Assertions.assertThat(found.getName()).isEqualTo("shop1");
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void findByNonId(){
        Assertions.assertThatThrownBy(()->shopService.findById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("이름 조회 확인")
    void findByName(){
        shopService.addShop(newShop("shop1"));

        Shop found = shopService.findByName("shop1");

        Assertions.assertThat(found.getName()).isEqualTo("shop1");
    }

    @Test
    @DisplayName("미존재 이름 조회 확인")
    void findByNonName(){
        Assertions.assertThatThrownBy(()->shopService.findByName("nobody"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("모든 shop 조회 확인")
    void findAll(){
        shopService.addShop(newShop("shop1"));
        shopService.addShop(newShop("shop2"));

        Assertions.assertThat(shopService.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("패치 적용 확인")
    void updateShop(){
        Shop saved = shopService.addShop(newShop("shop1"));

        ShopPatchRequest request = new ShopPatchRequest();
        request.setName("renamed");

        Shop updated = shopService.updateShop(saved.getId(), request);

        Assertions.assertThat(updated.getName()).isEqualTo("renamed");
    }

    @Test
    @DisplayName("미존재 id 패치 적용 확인")
    void updateShopByNonId(){
        ShopPatchRequest request = new ShopPatchRequest();
        request.setName("renamed");

        Assertions.assertThatThrownBy(()->shopService.updateShop(-1L, request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("제거 확인")
    void deleteShopById(){
        Shop saved = shopService.addShop(newShop("shop1"));

        shopService.deleteShopById(saved.getId());

        Assertions.assertThatThrownBy(()->shopService.findById(saved.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
