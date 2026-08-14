package com.toy.game_shop.dto.player;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Player;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"count","players"})
public class PlayerListResponse{
    private final int count;
    private final List<PlayerResponse> players;

    public PlayerListResponse(List<Player> players){
        this.count = players.size();
        this.players = players.stream().map(PlayerResponse::new).toList();
    }
}
