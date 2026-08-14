package com.toy.game_shop.controller;

import com.toy.game_shop.dto.player.PlayerCreateRequest;
import com.toy.game_shop.dto.player.PlayerListResponse;
import com.toy.game_shop.dto.player.PlayerResponse;
import com.toy.game_shop.entity.Player;
import com.toy.game_shop.dto.player.PlayerPatchRequest;
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
    public PlayerListResponse findAll(){
        List<Player> players = playerService.findAll();
        return new PlayerListResponse(players);
    }

    @GetMapping("{id}")
    public PlayerResponse findById(@PathVariable Long id){

        return new PlayerResponse(playerService.findById(id));
    }

    @GetMapping("nickname/{name}")
    public PlayerResponse findByNickname(@PathVariable String name){
        return new PlayerResponse(playerService.findByNickname(name));
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> addPlayer(
            @RequestBody @Valid PlayerCreateRequest request){

        Player player = Player.builder()
                .nickname(request.getNickname())
                .gold(request.getGold())
                .build();

        Player savedPlayer = playerService.addPlayer(player);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPlayer.getId())
                .toUri();

        return ResponseEntity.created(location).body(new PlayerResponse(savedPlayer));
    }

    @PatchMapping("{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long id, @RequestBody
                                            PlayerPatchRequest request){
        Player updated = playerService.updatePlayer(id,request);
        return ResponseEntity.ok(new PlayerResponse(updated));
    }

    @PatchMapping("nickname/{name}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable String name, @RequestBody
                                               PlayerPatchRequest request){
        Player updated = playerService.updatePlayer(name,request);
        return ResponseEntity.ok(new PlayerResponse(updated));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id){
        playerService.deletePlayerById(id);
        return ResponseEntity.noContent().build();
    }
}
