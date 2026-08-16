package com.toy.game_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Shop명 입력")
    @Size(min = 1, message = "Shop명 입력")
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "shop")
    private List<ShopStock> stocks;
}
