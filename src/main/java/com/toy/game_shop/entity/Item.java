package com.toy.game_shop.entity;

import com.toy.game_shop.type.ItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item", comment = "아이템 정의 테이블")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item명 입력")
    @Size(min = 1, message = "Item명 입력")
    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType type;

    @Size(max = 500)
    private String description;

    @Builder.Default
    @Min(1)
    private Integer maxStack = 99;
}
