package com.toy.game_shop.controller;

import com.toy.game_shop.dto.shopStock.ShopStockCreateRequest;
import com.toy.game_shop.dto.shopStock.ShopStockListResponse;
import com.toy.game_shop.dto.shopStock.ShopStockResponse;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.dto.shopStock.ShopStockPatchRequest;
import com.toy.game_shop.service.ItemService;
import com.toy.game_shop.service.ShopService;
import com.toy.game_shop.service.ShopStockService;
import jakarta.validation.Valid;
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
    public ShopStockListResponse findAllStockByShopId(@PathVariable Long shopId){
        List<ShopStock> shopStocks =  shopStockService.findByShopId(shopId);
        return new ShopStockListResponse(shopStocks);
    }

    @GetMapping("/{stockId}")
    public ShopStockResponse findStockByStockId(@PathVariable Long shopId,
                                                @PathVariable Long stockId){
        return new ShopStockResponse(shopStockService.findByShopStockId(shopId, stockId));
    }

    @GetMapping("/item/{itemId}")
    public ShopStockResponse findStockByShopIdAndItemId(@PathVariable Long shopId,
                                                @PathVariable Long itemId){
        Shop shop = shopService.findById(shopId);
        Item item = itemService.findById(itemId);

        return new ShopStockResponse(shopStockService.findByShopAndItem(shop,item));
    }

    @PostMapping
    public ResponseEntity<ShopStockResponse> addShopStock(@PathVariable Long shopId,
                                       @RequestParam Long itemId,
                                       @RequestBody @Valid ShopStockCreateRequest request){
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

        return ResponseEntity.created(location).body(new ShopStockResponse(saved));
    }

    @PatchMapping("{stockId}")
    public ResponseEntity<ShopStockResponse> updateShopStock(
            @PathVariable Long shopId, @PathVariable Long stockId,
            @RequestBody ShopStockPatchRequest request){
        ShopStock updated = shopStockService.updateShopStock(shopId,stockId,request);
        return ResponseEntity.ok(new ShopStockResponse(updated));
    }

    @PostMapping("{stockId}/increase")
    public ShopStockResponse increaseStock(@PathVariable Long shopId,
            @PathVariable Long stockId, @RequestParam Integer amount){
        return new ShopStockResponse(shopStockService.increaseStock(shopId,stockId,amount));
    }

    @PostMapping("{stockId}/decrease")
    public ShopStockResponse decreaseStock(@PathVariable Long shopId,
            @PathVariable Long stockId, @RequestParam Integer amount){
        return new ShopStockResponse(shopStockService.decreaseStock(shopId,stockId,amount));
    }

    @DeleteMapping("{stockId}")
    public ResponseEntity<Void> deleteStockByStockId(@PathVariable Long shopId,
                                                     @PathVariable Long stockId){
        shopStockService.deleteShopStockById(shopId,stockId);
        return ResponseEntity.noContent().build();
    }
}
