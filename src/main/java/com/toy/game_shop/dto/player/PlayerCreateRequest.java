package com.toy.game_shop.dto.player;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerCreateRequest{
    @NotBlank(message = "닉네임은 공백일 수 없습니다")
    @Size(min = 2, message = "2글자 이상의 캐릭명 입력")
    @Pattern(regexp = "^\\S+$", message = "닉네임에 공백을 포함할 수 없습니다")
    @Pattern(regexp = "^\\D.*$", message = "닉네임은 숫자로 시작할 수 없습니다")
    private String nickname;

    @NotNull
    @Min(0)
    private Long gold;
}
