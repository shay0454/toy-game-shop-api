package com.toy.game_shop.entity;

import com.toy.game_shop.type.ItemType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventory", uniqueConstraints = @UniqueConstraint(columnNames = {"player_id","index_row","index_col"}))
public class InventorySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NotNull
    @Min(0)
    @Max(99)
    private Integer quantity;

    @Min(1)
    @Max(6)
    @Column(name = "index_row")
    private Integer row;

    @Min(1)
    @Max(10)
    @Column(name = "index_col")
    private Integer col;
}
