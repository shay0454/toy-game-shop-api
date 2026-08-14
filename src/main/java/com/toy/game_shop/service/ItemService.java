package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.dto.item.ItemPatchRequest;
import com.toy.game_shop.repository.ItemRepository;
import com.toy.game_shop.type.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository it;

    public List<Item> findAll(){
        return it.findAll();
    }

    public Item findById(Long id){
        return it.findById(id)
                .orElseThrow(()->new NoSuchElementException("Item not found"));
    }

    public Item findByName(String name){
        return it.findByName(name)
                .orElseThrow(()->new NoSuchElementException("Item not found"));
    }

    public Item addItem(Item item){
        if(item.getType()==null){
            item.setType(ItemType.NONE);
        }

        return it.save(item);
    }

    public void deleteItemById(Long id){
        it.deleteById(id);
    }

    public List<Item> findByType(ItemType type){
        return it.findByType(type);
    }

    public Item updateItem(Long id, ItemPatchRequest request){
        Item item = it.findById(id)
                .orElseThrow(()->new NoSuchElementException("Item not found"));

        if(request.getName()!=null) item.setName(request.getName());
        if(request.getType()!=null) item.setType(request.getType());

        return it.save(item);
    }
}
