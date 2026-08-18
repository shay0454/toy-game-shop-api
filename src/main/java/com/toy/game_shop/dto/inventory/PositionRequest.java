package com.toy.game_shop.dto.inventory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PositionRequest {
    @NotNull
    @Min(1) @Max(6)
    private Integer row;

    @NotNull
    @Min(1) @Max(10)
    private Integer col;
}
