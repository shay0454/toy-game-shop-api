package com.toy.game_shop.controller;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.patchRequest.ItemPatchRequest;
import com.toy.game_shop.service.ItemService;
import com.toy.game_shop.type.ItemType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> findAll(){
        return itemService.findAll();
    }

    @GetMapping("{id}")
    public Item findById(@PathVariable Long id){
        return itemService.findById(id);
    }

    @GetMapping("/name/{name}")
    public Item findByName(@PathVariable String name){return itemService.findByName(name);}

    @GetMapping("/type/{type}")
    public List<Item> findByType(@PathVariable ItemType type){
        return itemService.findByType(type);
    }

    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody @Valid Item item){
        Item savedItem = itemService.addItem(item);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody
                                           ItemPatchRequest request){
        return ResponseEntity.ok(itemService.updateItem(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        itemService.deleteItemById(id);
        return ResponseEntity.noContent().build();
    }
}
