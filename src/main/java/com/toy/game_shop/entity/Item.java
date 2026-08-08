package com.toy.game_shop.entity;

import com.toy.game_shop.type.ItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, message = "Item명 입력")
    @Column(unique = true, nullable = false)
    private String name;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Long price;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType type;
}
