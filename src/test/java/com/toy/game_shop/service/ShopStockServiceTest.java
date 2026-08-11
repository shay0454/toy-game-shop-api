package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.patchRequest.ShopStockPatchRequest;
import com.toy.game_shop.repository.ItemRepository;
import com.toy.game_shop.repository.ShopRepository;
import com.toy.game_shop.type.ItemType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ShopStockService.class)
class ShopStockServiceTest {

    @Autowired
    private ShopStockService shopStockService;

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
    }

    private ShopStock newStock(Shop shop, Item item, Integer stock, Long price){
        return ShopStock.builder().shop(shop).item(item).stock(stock).price(price).build();
    }

    @Test
    @DisplayName("생성 확인")
    void addShopStock(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getStock()).isEqualTo(50);
        Assertions.assertThat(saved.getPrice()).isEqualTo(100L);
    }

    @Test
    @DisplayName("이미 등록된 상점-아이템 조합 생성 시 예외 확인")
    void addShopStockDuplicate(){
        shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Assertions.assertThatThrownBy(()->shopStockService.addShopStock(newStock(shop, item, 10, 200L)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findByShopStockId(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        ShopStock found = shopStockService.findByShopStockId(saved.getId());

        Assertions.assertThat(found.getStock()).isEqualTo(50);
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void findByShopStockIdNon(){
        Assertions.assertThatThrownBy(()->shopStockService.findByShopStockId(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("shopId+stockId로 소유권 확인 조회 확인")
    void findByShopStockIdWithOwnership(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        ShopStock found = shopStockService.findByShopStockId(shop.getId(), saved.getId());

        Assertions.assertThat(found.getStock()).isEqualTo(50);
    }

    @Test
    @DisplayName("다른 상점 소유 재고 조회 시 예외 확인")
    void findByShopStockIdWrongShop(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Shop otherShop = shopRepository.save(Shop.builder().name("shop2").build());

        Assertions.assertThatThrownBy(()->shopStockService.findByShopStockId(otherShop.getId(), saved.getId()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("shop, item으로 조회 확인")
    void findByShopAndItem(){
        shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        ShopStock found = shopStockService.findByShopAndItem(shop, item);

        Assertions.assertThat(found.getStock()).isEqualTo(50);
    }

    @Test
    @DisplayName("미존재 shop, item 조회 확인")
    void findByShopAndItemNon(){
        Assertions.assertThatThrownBy(()->shopStockService.findByShopAndItem(shop, item))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("shopId로 전체 재고 조회 확인")
    void findByShopId(){
        shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Item otherItem = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());
        shopStockService.addShopStock(newStock(shop, otherItem, 10, 200L));

        Assertions.assertThat(shopStockService.findByShopId(shop.getId()).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("재고 증가 확인")
    void increaseStock(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        ShopStock updated = shopStockService.increaseStock(shop.getId(), saved.getId(), 20);

        Assertions.assertThat(updated.getStock()).isEqualTo(70);
    }

    @Test
    @DisplayName("증가량 0 이하 시 예외 확인")
    void increaseStockNonPositive(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Assertions.assertThatThrownBy(()->shopStockService.increaseStock(shop.getId(), saved.getId(), 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("재고 감소 확인")
    void decreaseStock(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        ShopStock updated = shopStockService.decreaseStock(shop.getId(), saved.getId(), 20);

        Assertions.assertThat(updated.getStock()).isEqualTo(30);
    }

    @Test
    @DisplayName("재고 부족 시 예외 확인")
    void decreaseStockInsufficient(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 10, 100L));

        Assertions.assertThatThrownBy(()->shopStockService.decreaseStock(shop.getId(), saved.getId(), 20))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("감소량 0 이하 시 예외 확인")
    void decreaseStockNonPositive(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Assertions.assertThatThrownBy(()->shopStockService.decreaseStock(shop.getId(), saved.getId(), 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("패치 적용 확인")
    void updateShopStock(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        ShopStockPatchRequest request = new ShopStockPatchRequest();
        request.setPrice(200L);

        ShopStock updated = shopStockService.updateShopStock(shop.getId(), saved.getId(), request);

        Assertions.assertThat(updated.getPrice()).isEqualTo(200L);
        Assertions.assertThat(updated.getStock()).isEqualTo(50);
    }

    @Test
    @DisplayName("다른 상점 소유 재고 패치 시 예외 확인")
    void updateShopStockWrongShop(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Shop otherShop = shopRepository.save(Shop.builder().name("shop2").build());

        ShopStockPatchRequest request = new ShopStockPatchRequest();
        request.setPrice(200L);

        Assertions.assertThatThrownBy(()->shopStockService.updateShopStock(otherShop.getId(), saved.getId(), request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("제거 확인")
    void deleteShopStockById(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        shopStockService.deleteShopStockById(shop.getId(), saved.getId());

        Assertions.assertThatThrownBy(()->shopStockService.findByShopStockId(saved.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("다른 상점 소유 재고 삭제 시 예외 확인")
    void deleteShopStockByIdWrongShop(){
        ShopStock saved = shopStockService.addShopStock(newStock(shop, item, 50, 100L));

        Shop otherShop = shopRepository.save(Shop.builder().name("shop2").build());

        Assertions.assertThatThrownBy(()->shopStockService.deleteShopStockById(otherShop.getId(), saved.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
