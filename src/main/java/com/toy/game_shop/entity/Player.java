package com.toy.game_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=2, message = "2글자 이상의 캐릭명 입력")
    @Column(unique = true, nullable = false)
    private String nickname;

    @Min(0)
    @Column(nullable = false)
    private Long gold;

    @Version
    private Long version;
}
