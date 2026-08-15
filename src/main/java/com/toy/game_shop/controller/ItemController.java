package com.toy.game_shop.controller;

import com.toy.game_shop.dto.item.ItemCreateRequest;
import com.toy.game_shop.dto.item.ItemListResponse;
import com.toy.game_shop.dto.item.ItemResponse;
import com.toy.game_shop.entity.Item;
import com.toy.game_shop.dto.item.ItemPatchRequest;
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
    public ItemListResponse findAll(){
        List<Item> items = itemService.findAll();
        ItemListResponse result = new ItemListResponse(items);
        return result;
    }

    @GetMapping("{id}")
    public ItemResponse findById(@PathVariable Long id){
        ItemResponse result = new ItemResponse(itemService.findById(id));
        return result;
    }

    @GetMapping("/name/{name}")
    public ItemResponse findByName(@PathVariable String name){
        ItemResponse result = new ItemResponse(itemService.findByName(name));
        return result;
    }

    @GetMapping("/type/{type}")
    public ItemListResponse findByType(@PathVariable ItemType type){
        List<Item> items = itemService.findByType(type);
        ItemListResponse result = new ItemListResponse(items);
        return result;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> addItem(
            @RequestBody @Valid ItemCreateRequest request){
        Item item = Item.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .maxStack(request.getMaxStack())
                .build();

        Item savedItem = itemService.addItem(item);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.getId())
                .toUri();
        return ResponseEntity.created(location).body(new ItemResponse(savedItem));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @RequestBody @Valid
                                           ItemPatchRequest request){
        Item updated = itemService.updateItem(id,request);
        return ResponseEntity.ok(new ItemResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        itemService.deleteItemById(id);
        return ResponseEntity.noContent().build();
    }
}
