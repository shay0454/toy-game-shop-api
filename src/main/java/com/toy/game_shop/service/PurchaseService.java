package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PlayerService playerService;
    private final ShopStockService shopStockService;
    private final InventoryService inventoryService;
    private final TransactionService transactionService;

    @Transactional
    public Transaction purchase(Player player, Shop shop, Item item, Integer quantity){

        // 음수 확인
        if(quantity <= 0){
            throw new IllegalArgumentException("quantity는 0보다 커야 함");
        }

        ShopStock shopStock = shopStockService.findByShopAndItem(shop, item);
        long totalPrice = shopStock.getPrice() * quantity;

        playerService.spendGold(player.getId(), totalPrice);
        shopStockService.decreaseStock(shop.getId(), shopStock.getId(), quantity);
        inventoryService.addQuantity(player, item, quantity);

        Transaction transaction = Transaction.builder()
                .player(player)
                .shop(shop)
                .item(item)
                .quantity(quantity)
                .priceAtTransaction(shopStock.getPrice())
                .build();

        return transactionService.addTransaction(transaction);
    }
}
