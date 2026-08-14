package com.toy.game_shop.controller;

import com.toy.game_shop.dto.purchase.PurchaseRequest;
import com.toy.game_shop.dto.transaction.TransactionResponse;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.Transaction;
import com.toy.game_shop.service.ItemService;
import com.toy.game_shop.service.PlayerService;
import com.toy.game_shop.service.PurchaseService;
import com.toy.game_shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players/{playerId}/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PlayerService playerService;
    private final ShopService shopService;
    private final ItemService itemService;

    @PostMapping
    public TransactionResponse purchase(@PathVariable Long playerId,
                                 @RequestBody PurchaseRequest request){
        Player player = playerService.findById(playerId);
        Shop shop = shopService.findById(request.getShopId());
        Item item = itemService.findById(request.getItemId());

        Transaction transaction = purchaseService.purchase(player, shop, item, request.getQuantity());
        return new TransactionResponse(transaction);
    }
}
