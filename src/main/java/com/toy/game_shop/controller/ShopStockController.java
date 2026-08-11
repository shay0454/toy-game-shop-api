package com.toy.game_shop.controller;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.patchRequest.ShopStockPatchRequest;
import com.toy.game_shop.service.ItemService;
import com.toy.game_shop.service.ShopService;
import com.toy.game_shop.service.ShopStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops/{shopId}/stock")
public class ShopStockController {
    private final ItemService itemService;
    private final ShopService shopService;
    private final ShopStockService shopStockService;

    @GetMapping
    public List<ShopStock> findAllStockByShopId(@PathVariable Long shopId){
        return shopStockService.findByShopId(shopId);
    }

    @GetMapping("/{stockId}")
    public ShopStock findStockByStockId(@PathVariable Long shopId,
                                        @PathVariable Long stockId){
        return shopStockService.findByShopStockId(shopId, stockId);
    }

    @GetMapping("/item/{itemId}")
    public ShopStock findStockByShopIdAndItemId(@PathVariable Long shopId,
                                                @PathVariable Long itemId){
        Shop shop = shopService.findById(shopId);
        Item item = itemService.findById(itemId);

        return shopStockService.findByShopAndItem(shop,item);
    }

    @PostMapping
    public ResponseEntity<ShopStock> addShopStock(@PathVariable Long shopId,
                                       @RequestParam Long itemId,
                                       @RequestBody ShopStockPatchRequest request){
        Shop shop = shopService.findById(shopId);
        Item item = itemService.findById(itemId);

        ShopStock stock = ShopStock.builder()
                .shop(shop)
                .item(item)
                .stock(request.getStock())
                .price(request.getPrice())
                .build();

        ShopStock saved = shopStockService.addShopStock(stock);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{stockId}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{stockId}")
    public ResponseEntity<ShopStock> updateShopStock(
            @PathVariable Long shopId, @PathVariable Long stockId,
            @RequestBody ShopStockPatchRequest request){
        return ResponseEntity.ok(shopStockService.updateShopStock(shopId,stockId,request));
    }

    @PostMapping("{stockId}/increase")
    public ShopStock increaseStock(@PathVariable Long shopId,
            @PathVariable Long stockId, @RequestParam Integer amount){
        return shopStockService.increaseStock(shopId,stockId,amount);
    }

    @PostMapping("{stockId}/decrease")
    public ShopStock decreaseStock(@PathVariable Long shopId,
            @PathVariable Long stockId, @RequestParam Integer amount){
        return shopStockService.decreaseStock(shopId,stockId,amount);
    }

    @DeleteMapping("{stockId}")
    public ResponseEntity<Void> deleteStockByStockId(@PathVariable Long shopId,
                                                     @PathVariable Long stockId){
        shopStockService.deleteShopStockById(shopId,stockId);
        return ResponseEntity.noContent().build();
    }
}
