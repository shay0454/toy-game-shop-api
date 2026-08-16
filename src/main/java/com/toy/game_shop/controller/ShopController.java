package com.toy.game_shop.controller;

import com.toy.game_shop.dto.shop.*;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class
ShopController {
    private final ShopService shopService;

    @GetMapping
    public ShopListResponse findAll(){
        List<Shop> shops = shopService.findAll();
        return new ShopListResponse(shops);
    }

    @GetMapping("{id}")
    public ShopResponse findById(@PathVariable Long id){
        return new ShopResponse(shopService.findById(id));
    }

    @GetMapping("/name/{name}")
    public ShopResponse findByName(@PathVariable String name){

        return new ShopResponse(shopService.findByName(name));
    }

    @GetMapping("{id}/detail")
    public ShopDetailResponse findByIdWithDetails(@PathVariable Long id){
        return new ShopDetailResponse(shopService.findByIdWithDetail(id));
    }

    @PostMapping
    public ResponseEntity<ShopResponse> addShop(@RequestBody @Valid ShopCreateRequest request){
        Shop shop = Shop.builder()
                .name(request.getName())
                .build();

        Shop savedShop = shopService.addShop(shop);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedShop.getId())
                .toUri();

        return ResponseEntity.created(location).body(new ShopResponse(savedShop));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ShopResponse> updateShop(@PathVariable Long id, @RequestBody @Valid ShopPatchRequest request){
        Shop updated = shopService.updateShop(id, request);
        return ResponseEntity.ok(new ShopResponse(updated));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id){
        shopService.deleteShopById(id);
        return ResponseEntity.noContent().build();
    }
}
