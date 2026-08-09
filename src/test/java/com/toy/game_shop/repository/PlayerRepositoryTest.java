package com.toy.game_shop.repository;

import com.toy.game_shop.entity.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    private Player test1;

    @BeforeEach
    void before(){
        test1 = playerRepository.save(Player.builder().nickname("test1").gold(0L).build());
        playerRepository.save(Player.builder().nickname("test2").gold(100L).build());
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findPlayerById(){
        Optional<Player> player = playerRepository.findById(test1.getId());

        Assertions.assertThat(player).isPresent();
        Assertions.assertThat(player.get().getNickname()).isEqualTo("test1");
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void findPlayerByNonId(){
        Optional<Player> player = playerRepository.findById(-1L);
        Assertions.assertThat(player).isEmpty();
    }

    @Test
    @DisplayName("닉네임으로 조회 확인")
    void findPlayerByNickname(){
        Optional<Player> player = playerRepository.findByNickname("test1");

        Assertions.assertThat(player).isPresent();
        Assertions.assertThat(player.get().getNickname()).isEqualTo("test1");
    }

    @Test
    @DisplayName("미존재 닉네임 조회 확인")
    void findPlayerByNonNickname(){
        Optional<Player> player = playerRepository.findByNickname("nobody");
        Assertions.assertThat(player).isEmpty();
    }

    @Test
    @DisplayName("삭제 확인")
    void deletePlayerById(){
        playerRepository.deleteById(test1.getId());

        Optional<Player> player = playerRepository.findById(test1.getId());
        Assertions.assertThat(player).isEmpty();
    }
}
