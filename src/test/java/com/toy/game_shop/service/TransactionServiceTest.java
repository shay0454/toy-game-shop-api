package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.Transaction;
import com.toy.game_shop.repository.ItemRepository;
import com.toy.game_shop.repository.PlayerRepository;
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
@Import(TransactionService.class)
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

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
    }

    private Transaction newTransaction(){
        return Transaction.builder()
                .player(player).item(item).shop(shop)
                .quantity(1).priceAtTransaction(100L).build();
    }

    @Test
    @DisplayName("생성 확인")
    void addTransaction(){
        Transaction saved = transactionService.addTransaction(newTransaction());

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findTransactionById(){
        Transaction saved = transactionService.addTransaction(newTransaction());

        Transaction found = transactionService.findTransactionById(saved.getId());

        Assertions.assertThat(found.getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void findTransactionByNonId(){
        Assertions.assertThatThrownBy(()->transactionService.findTransactionById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("전체 조회 확인")
    void findAllTransactions(){
        transactionService.addTransaction(newTransaction());
        transactionService.addTransaction(newTransaction());

        Assertions.assertThat(transactionService.findAllTransactions().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("player로 조회 확인")
    void findTransactionsByPlayer(){
        transactionService.addTransaction(newTransaction());

        Assertions.assertThat(transactionService.findTransactionsByPlayer(player).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("item으로 조회 확인")
    void findTransactionsByItem(){
        transactionService.addTransaction(newTransaction());

        Assertions.assertThat(transactionService.findTransactionsByItem(item).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("player, item으로 조회 확인")
    void findTransactionsByPlayerAndItem(){
        transactionService.addTransaction(newTransaction());

        Assertions.assertThat(transactionService.findTransactionsByPlayerAndItem(player, item).size()).isEqualTo(1);
    }
}
