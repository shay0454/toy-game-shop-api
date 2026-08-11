package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Shop;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ShopRepositoryTest {

    @Autowired
    private ShopRepository shopRepository;

    private Shop shop1;

    @BeforeEach
    void before(){
        shop1 = shopRepository.save(Shop.builder().name("shop1").build());
        shopRepository.save(Shop.builder().name("shop2").build());
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findShopById(){
        Optional<Shop> shop = shopRepository.findById(shop1.getId());

        Assertions.assertThat(shop).isPresent();
        Assertions.assertThat(shop.get().getName()).isEqualTo("shop1");
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void findShopByNonId(){
        Optional<Shop> shop = shopRepository.findById(-1L);
        Assertions.assertThat(shop).isEmpty();
    }

    @Test
    @DisplayName("이름으로 조회 확인")
    void findShopByName(){
        Optional<Shop> shop = shopRepository.findByName("shop1");

        Assertions.assertThat(shop).isPresent();
        Assertions.assertThat(shop.get().getName()).isEqualTo("shop1");
    }

    @Test
    @DisplayName("미존재 이름 조회 확인")
    void findShopByNonName(){
        Optional<Shop> shop = shopRepository.findByName("nobody");
        Assertions.assertThat(shop).isEmpty();
    }

    @Test
    @DisplayName("삭제 확인")
    void deleteShopById(){
        shopRepository.deleteById(shop1.getId());

        Optional<Shop> shop = shopRepository.findById(shop1.getId());
        Assertions.assertThat(shop).isEmpty();
    }
}
