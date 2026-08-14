package com.toy.game_shop.service;

import com.toy.game_shop.entity.Player;
import com.toy.game_shop.dto.player.PlayerPatchRequest;
import com.toy.game_shop.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public List<Player> findAll(){
        return playerRepository.findAll();
    }

    public Player findById(Long id){
        return playerRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Player not found"));
    }

    public Player findByNickname(String name){
        return playerRepository.findByNickname(name)
                .orElseThrow(()->new NoSuchElementException("Player not found"));
    }

    public Player addPlayer(Player player){
        if(player.getGold()==null){
            player.setGold(0L);
        }
        return playerRepository.save(player);
    }

    public Player updatePlayer(Long id, PlayerPatchRequest request){
        Player player = playerRepository.findById(id)
                .orElseThrow(()->new NoSuchElementException("Player not found"));

        return applyPatch(player,request);
    }

    public Player updatePlayer(String name, PlayerPatchRequest request){
        Player player = playerRepository.findByNickname(name)
                .orElseThrow(()->new NoSuchElementException("Player not found"));

        return applyPatch(player,request);
    }

    private Player applyPatch(Player player, PlayerPatchRequest request){
        if(request.getNickname()!=null)player.setNickname(request.getNickname());
        if(request.getGold()!=null)player.setGold(request.getGold());
        return playerRepository.save(player);
    }

    public void deletePlayerById(Long id){
        playerRepository.deleteById(id);
    }

    public Player spendGold(Long id, Long amount){
        if(amount <= 0){
            throw new IllegalArgumentException("amount는 0보다 커야 함");
        }

        Player player = findById(id);
        long remaining = player.getGold() - amount;
        if(remaining < 0){
            throw new IllegalStateException("골드 부족");
        }

        player.setGold(remaining);
        return playerRepository.save(player);
    }

}
