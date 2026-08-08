package com.toy.game_shop.controller;

import com.toy.game_shop.entity.Player;
import com.toy.game_shop.patchRequest.PlayerPatchRequest;
import com.toy.game_shop.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public List<Player> findAll(){ return playerService.findAll();}

    @GetMapping("{id}")
    public Player findById(@PathVariable Long id){
        return playerService.findById(id);
    }

    @GetMapping("nickname/{name}")
    public Player findByNickname(@PathVariable String name){
        return playerService.findByNickname(name);
    }

    @PostMapping
    public ResponseEntity<Player> addPlayer(@RequestBody @Valid Player player){
        Player savedPlayer = playerService.addPlayer(player);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPlayer.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody
                                            PlayerPatchRequest request){
        return ResponseEntity.ok(playerService.updatePlayer(id,request));
    }

    @PatchMapping("nickname/{name}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String name, @RequestBody
                                               PlayerPatchRequest request){
        return ResponseEntity.ok(playerService.updatePlayer(name,request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long id){
        playerService.deletePlayerById(id);
        return ResponseEntity.noContent().build();
    }

}
