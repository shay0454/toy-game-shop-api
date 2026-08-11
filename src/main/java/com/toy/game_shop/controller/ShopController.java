package com.toy.game_shop.controller;

import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.patchRequest.ShopPatchRequest;
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
    public List<Shop> findAll(){
        return shopService.findAll();
    }

    @GetMapping("{id}")
    public Shop findById(@PathVariable Long id){
        return shopService.findById(id);
    }

    @GetMapping("/name/{name}")
    public Shop findByName(@PathVariable String name){
        return shopService.findByName(name);
    }

    @PostMapping
    public ResponseEntity<Shop> addShop(@RequestBody @Valid Shop shop){
        Shop savedShop = shopService.addShop(shop);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedShop.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody ShopPatchRequest request){
        return ResponseEntity.ok(shopService.updateShop(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id){
        shopService.deleteShopById(id);
        return ResponseEntity.noContent().build();
    }
}
