package com.toy.game_shop.config;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.repository.ItemRepository;
import com.toy.game_shop.repository.PlayerRepository;
import com.toy.game_shop.repository.ShopRepository;
import com.toy.game_shop.repository.ShopStockRepository;
import com.toy.game_shop.type.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;
    private final ShopStockRepository shopStockRepository;

    @Override
    public void run(String... args){
        if(playerRepository.count() > 0){
            return;
        }

        playerRepository.save(Player.builder().nickname("tester").gold(10000L).build());

        Item sword = itemRepository.save(Item.builder()
                .name("Sword").type(ItemType.WEAPON).description("기본 검").maxStack(1).build());
        Item potion = itemRepository.save(Item.builder()
                .name("Potion").type(ItemType.CONSUMABLE).description("체력 회복 물약").maxStack(99).build());
        Item armor = itemRepository.save(Item.builder()
                .name("Leather Armor").type(ItemType.ARMOR).description("가죽 갑옷").maxStack(1).build());

        Shop shop = shopRepository.save(Shop.builder().name("General Store").build());

        shopStockRepository.save(ShopStock.builder().shop(shop).item(sword).stock(10).price(500L).build());
        shopStockRepository.save(ShopStock.builder().shop(shop).item(potion).stock(50).price(20L).build());
        shopStockRepository.save(ShopStock.builder().shop(shop).item(armor).stock(5).price(300L).build());
    }
}
