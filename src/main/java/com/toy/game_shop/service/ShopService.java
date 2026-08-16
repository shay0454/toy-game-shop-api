package com.toy.game_shop.service;

import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.dto.shop.ShopPatchRequest;
import com.toy.game_shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;

    public List<Shop> findAll(){
        return shopRepository.findAll();
    }

    public Shop findById(Long id){
        return shopRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Shop not found"));
    }

    public Shop findByName(String name){
        return shopRepository.findByName(name)
                .orElseThrow(()->new NoSuchElementException("Shop not found"));
    }

    public Shop findByIdWithDetail(Long id){
        return shopRepository.findByIdWithStocks(id)
                .orElseThrow(()->new NoSuchElementException("Shop not found"));
    }

    public Shop addShop(Shop shop){
        return shopRepository.save(shop);
    }

    public Shop updateShop(Long id, ShopPatchRequest request){
        Shop shop = shopRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Shop not found"));

        if(request.getName()!=null) shop.setName(request.getName());

        return shopRepository.save(shop);
    }

    public void deleteShopById(Long id){
        shopRepository.deleteById(id);
    }
}
