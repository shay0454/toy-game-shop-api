package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.type.ItemType;
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
class ShopStockRepositoryTest {

    @Autowired
    private ShopStockRepository shopStockRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Shop shop;
    private Item item;

    @BeforeEach
    void before(){
        shop = shopRepository.save(Shop.builder().name("shop1").build());
        item = itemRepository.save(Item.builder().name("Potion").type(ItemType.CONSUMABLE).build());

        shopStockRepository.save(ShopStock.builder().shop(shop).item(item).stock(50).price(100L).build());
    }

    @Test
    @DisplayName("shop, item으로 조회 확인")
    void findByShopAndItem(){
        Optional<ShopStock> stock = shopStockRepository.findByShopAndItem(shop, item);

        Assertions.assertThat(stock).isPresent();
        Assertions.assertThat(stock.get().getStock()).isEqualTo(50);
    }

    @Test
    @DisplayName("미존재 shop, item 조회 확인")
    void findByShopAndItemNon(){
        Item otherItem = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());

        Optional<ShopStock> stock = shopStockRepository.findByShopAndItem(shop, otherItem);

        Assertions.assertThat(stock).isEmpty();
    }

    @Test
    @DisplayName("shopId로 전체 재고 조회 확인")
    void findByShopId(){
        Item otherItem = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());
        shopStockRepository.save(ShopStock.builder().shop(shop).item(otherItem).stock(10).price(200L).build());

        List<ShopStock> stocks = shopStockRepository.findByShopId(shop.getId());

        Assertions.assertThat(stocks.size()).isEqualTo(2);
    }
}
