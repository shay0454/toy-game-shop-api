package com.toy.game_shop.dto.player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerPatchRequest {
    private String nickname;
    private Long gold;
}
