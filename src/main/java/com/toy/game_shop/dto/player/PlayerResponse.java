package com.toy.game_shop.dto.player;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.toy.game_shop.entity.Player;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"id","nickname","gold"})
public class PlayerResponse {
    private final Long id;
    private final String nickname;
    private final Long gold;

    public PlayerResponse(Player player){
        this.id = player.getId();
        this.nickname = player.getNickname();
        this.gold = player.getGold();
    }
}
