package com.toy.game_shop.patchRequest;

import com.toy.game_shop.type.ItemType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPatchRequest {
    private String name;
    private ItemType type;
}
