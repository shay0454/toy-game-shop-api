package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.Transaction;
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
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShopRepository shopRepository;

    private Player player;
    private Item item;
    private Shop shop;

    @BeforeEach
    void before(){
        player = playerRepository.save(Player.builder().nickname("tester").gold(0L).build());
        item = itemRepository.save(Item.builder().name("Potion").type(ItemType.CONSUMABLE).build());
        shop = shopRepository.save(Shop.builder().name("shop1").build());

        transactionRepository.save(Transaction.builder()
                .player(player).item(item).shop(shop)
                .quantity(1).priceAtTransaction(100L).build());
    }

    @Test
    @DisplayName("player로 조회 확인")
    void findByPlayer(){
        List<Transaction> transactions = transactionRepository.findByPlayer(player);

        Assertions.assertThat(transactions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("item으로 조회 확인")
    void findByItem(){
        List<Transaction> transactions = transactionRepository.findByItem(item);

        Assertions.assertThat(transactions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("player, item으로 조회 확인")
    void findByPlayerAndItem(){
        List<Transaction> transactions = transactionRepository.findByPlayerAndItem(player, item);

        Assertions.assertThat(transactions.size()).isEqualTo(1);
    }
}
