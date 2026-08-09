package com.toy.game_shop.service;

import com.toy.game_shop.entity.Player;
import com.toy.game_shop.patchRequest.PlayerPatchRequest;
import com.toy.game_shop.repository.PlayerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PlayerService.class)
class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;

    private Player newPlayer(String name,Long gold){
        return Player.builder()
                .nickname(name)
                .gold(gold)
                .build();
    }

    @Test
    @DisplayName("모든 player 조회 확인")
    void findAll() {
        playerService.addPlayer(newPlayer("test1",0L));
        playerService.addPlayer(newPlayer("test2",2L));
        playerService.addPlayer(newPlayer("test3",3L));

        Assertions.assertThat(playerService.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Id 조회 확인")
    void findById() {
        Player player = playerService.addPlayer(newPlayer("test1",0L));

        Assertions.assertThat(playerService.findById(player.getId())
                .getNickname()).isEqualTo("test1");
        Assertions.assertThat(playerService.findById(player.getId())
                .getGold()).isEqualTo(0L);
    }

    @Test
    @DisplayName("미존재 Id 조회 확인")
    void FindByNonId(){
        Assertions.assertThatThrownBy(()->playerService.findById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("닉네임 조회 확인")
    void findByNickname() {
        Player player = playerService.addPlayer(newPlayer("test1",0L));

        Assertions.assertThat(playerService.findByNickname("test1")
                .getNickname()).isEqualTo("test1");
        Assertions.assertThat(playerService.findByNickname("test1")
                .getGold()).isEqualTo(0L);
    }

    @Test
    @DisplayName("미존재 닉네임 조회 확인")
    void findByNonNickname(){
        Assertions.assertThatThrownBy(()->playerService.findByNickname("nobody"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("생성 확인")
    void addPlayer() {
        Player saved = playerService.addPlayer(newPlayer("test1",100L));

        Assertions.assertThat(saved.getId()).isNotNull();
        Assertions.assertThat(saved.getNickname()).isEqualTo("test1");
        Assertions.assertThat(saved.getGold()).isEqualTo(100L);
    }

    @Test
    @DisplayName("gold 설정 없을 시, 0 확인")
    void addPlayerGoldNull() {
        Player saved = playerService.addPlayer(newPlayer("test1",null));

        Assertions.assertThat(saved.getGold()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Id로 패치 적용 확인")
    void updatePlayer() {
        Player player = playerService.addPlayer(newPlayer("test1",0L));

        PlayerPatchRequest request = new PlayerPatchRequest();
        request.setGold(500L);

        Player updated = playerService.updatePlayer(player.getId(),request);

        Assertions.assertThat(updated.getNickname()).isEqualTo("test1");
        Assertions.assertThat(updated.getGold()).isEqualTo(500L);
    }

    @Test
    @DisplayName("미존재 id 패치 적용 확인")
    void updatePlayerByNonId(){
        PlayerPatchRequest request = new PlayerPatchRequest();
        request.setGold(500L);

        Assertions.assertThatThrownBy(()->playerService.updatePlayer(-1L,request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("닉네임으로 패치 적용 확인")
    void testUpdatePlayer() {
        Player player = playerService.addPlayer(newPlayer("test1",0L));

        PlayerPatchRequest request = new PlayerPatchRequest();
        request.setGold(500L);

        Player updated = playerService.updatePlayer(player.getNickname(),request);

        Assertions.assertThat(updated.getNickname()).isEqualTo("test1");
        Assertions.assertThat(updated.getGold()).isEqualTo(500L);
    }

    @Test
    @DisplayName("미존재 닉네임 패치 적용 확인")
    void updatePlayerByNonNickname(){
        PlayerPatchRequest request = new PlayerPatchRequest();
        request.setGold(500L);

        Assertions.assertThatThrownBy(()->playerService.updatePlayer("nobody",request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("제거 확인")
    void deletePlayerById() {
        Player player = playerService.addPlayer(newPlayer("test1",0L));

        playerService.deletePlayerById(player.getId());

        Assertions.assertThatThrownBy(()->playerService.findById(player.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}