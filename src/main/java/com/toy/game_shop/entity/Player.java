package com.toy.game_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "player", comment = "기본 플레이어 정보 테이블")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "닉네임은 공백일 수 없습니다")
    @Size(min=2, message = "2글자 이상의 캐릭명 입력")
    @Pattern(regexp = "^\\S+$", message = "닉네임에 공백을 포함할 수 없습니다")
    @Pattern(regexp = "^\\D.*$", message = "닉네임은 숫자로 시작할 수 없습니다")
    @Column(unique = true, nullable = false)
    private String nickname;

    @Min(0)
    @Column(nullable = false)
    private Long gold;

    @Version
    private Long version;
}
