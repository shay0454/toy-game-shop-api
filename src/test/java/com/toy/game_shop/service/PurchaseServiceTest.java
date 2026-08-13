package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.entity.Transaction;
import com.toy.game_shop.repository.ItemRepository;
import com.toy.game_shop.repository.PlayerRepository;
import com.toy.game_shop.repository.ShopRepository;
import com.toy.game_shop.repository.ShopStockRepository;
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
@Import({PurchaseService.class, PlayerService.class, ShopStockService.class,
        InventoryService.class, TransactionService.class})
class PurchaseServiceTest {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopStockRepository shopStockRepository;

    private Player player;
    private Item item;
    private Shop shop;
    private ShopStock shopStock;

    @BeforeEach
    void before(){
        player = playerRepository.save(Player.builder().nickname("tester").gold(100000L).build());
        item = itemRepository.save(Item.builder().name("Potion").type(ItemType.CONSUMABLE).build());
        shop = shopRepository.save(Shop.builder().name("shop1").build());
        shopStock = shopStockRepository.save(ShopStock.builder()
                .shop(shop).item(item).stock(10).price(100L).build());
    }

    @Test
    @DisplayName("정상 구매 확인")
    void purchase(){
        Transaction transaction = purchaseService.purchase(player, shop, item, 2);

        Assertions.assertThat(transaction.getId()).isNotNull();
        Assertions.assertThat(transaction.getQuantity()).isEqualTo(2);
        Assertions.assertThat(transaction.getPriceAtTransaction()).isEqualTo(100L);

        Player updatedPlayer = playerRepository.findById(player.getId()).orElseThrow();
        Assertions.assertThat(updatedPlayer.getGold()).isEqualTo(99800L);

        ShopStock updatedStock = shopStockRepository.findById(shopStock.getId()).orElseThrow();
        Assertions.assertThat(updatedStock.getStock()).isEqualTo(8);
    }

    @Test
    @DisplayName("수량 0 이하 시 예외 확인")
    void purchaseNonPositiveQuantity(){
        Assertions.assertThatThrownBy(()->purchaseService.purchase(player, shop, item, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("해당 상점에 없는 아이템 구매 시 예외 확인")
    void purchaseNonExistentShopStock(){
        Item otherItem = itemRepository.save(Item.builder().name("Sword").type(ItemType.WEAPON).build());

        Assertions.assertThatThrownBy(()->purchaseService.purchase(player, shop, otherItem, 1))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("골드 부족 시 예외 확인")
    void purchaseInsufficientGold(){
        Player poorPlayer = playerRepository.save(Player.builder().nickname("poor").gold(50L).build());

        Assertions.assertThatThrownBy(()->purchaseService.purchase(poorPlayer, shop, item, 1))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("재고 부족 시 예외 확인")
    void purchaseInsufficientStock(){
        Assertions.assertThatThrownBy(()->purchaseService.purchase(player, shop, item, 20))
                .isInstanceOf(IllegalStateException.class);
    }
}
