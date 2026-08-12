package com.toy.game_shop.service;

import com.toy.game_shop.entity.Item;
import com.toy.game_shop.entity.Shop;
import com.toy.game_shop.entity.ShopStock;
import com.toy.game_shop.patchRequest.ShopStockPatchRequest;
import com.toy.game_shop.repository.ShopStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ShopStockService {

    private final ShopStockRepository shopStockRepository;

    public List<ShopStock> findByShopId(Long id){
        return shopStockRepository.findByShopId(id);
    }

    public ShopStock findByShopStockId(Long id){
        return shopStockRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Shop Item not found"));
    }

    public ShopStock findByShopStockId(Long shopId, Long stockId){
        ShopStock stock = findByShopStockId(stockId);
        if(!stock.getShop().getId().equals(shopId)){
            throw new IllegalStateException("해당 상점의 재고가 아님");
        }
        return stock;
    }

    public ShopStock findByShopAndItem(Shop shop, Item item){
        return shopStockRepository.findByShopAndItem(shop,item)
                .orElseThrow(()->new NoSuchElementException("Shop Item not found"));
    }

    public ShopStock addShopStock(ShopStock stock){
        if(shopStockRepository.findByShopAndItem(stock.getShop(),
                stock.getItem()).isPresent()){
            throw new IllegalStateException("이미 등록된 상점-아이템 조합");
        }

        return shopStockRepository.save(stock);
    }

    public ShopStock increaseStock(Long shopId, Long stockId, Integer amount){
        if(amount <= 0){
            throw new IllegalArgumentException("amount는 0보다 커야 함");
        }

        ShopStock stock = findByShopStockId(shopId, stockId);
        stock.setStock(stock.getStock() + amount);

        return shopStockRepository.save(stock);
    }

    public ShopStock decreaseStock(Long shopId, Long stockId, Integer amount){
        if(amount <= 0){
            throw new IllegalArgumentException("amount는 0보다 커야 함");
        }

        ShopStock stock = findByShopStockId(shopId, stockId);
        int remaining = stock.getStock() - amount;
        if(remaining < 0){
            throw new IllegalStateException("재고 부족");
        }

        stock.setStock(remaining);
        return shopStockRepository.save(stock);
    }

    public ShopStock updateShopStock(Long shopId, Long stockId, ShopStockPatchRequest request){
        ShopStock stock = findByShopStockId(shopId, stockId);

        if(request.getStock()!=null) stock.setStock(request.getStock());
        if(request.getPrice()!=null) stock.setPrice(request.getPrice());

        return shopStockRepository.save(stock);
    }

    public void deleteShopStockById(Long shopId, Long stockId){
        findByShopStockId(shopId, stockId);
        shopStockRepository.deleteById(stockId);
    }
}
