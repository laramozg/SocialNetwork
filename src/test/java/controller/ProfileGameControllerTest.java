package controller;

import org.example.controller.ProfileGameController;
import org.example.dto.GameDto;
import org.example.service.ProfileGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileGameControllerTest {

    @Mock
    private ProfileGameService profileGameService;

    @InjectMocks
    private ProfileGameController profileGameController;

    private List<GameDto> gameDtos;

    @BeforeEach
    public void setUp() {
        gameDtos = new ArrayList<>();
        GameDto gameDto = new GameDto();
        gameDto.setId(1);
        gameDto.setTitle("Test Game");
        gameDto.setGenre("Test Genre");
        gameDtos.add(gameDto);
    }

    @Test
    void testCreateProfileGame() {
        Integer profileId = 1;
        Integer gameId = 1;

        ResponseEntity<String> response = profileGameController.createProfileGame(profileId, gameId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("done", response.getBody());
        verify(profileGameService).addProfileToGame(profileId, gameId);
    }

    @Test
    void testGetAllGamesByProfileId() {
        Integer profileId = 1;
        when(profileGameService.getAllGamesByProfileId(profileId)).thenReturn(gameDtos);

        ResponseEntity<List<GameDto>> response = profileGameController.getAllGamesByProfileId(profileId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameDtos, response.getBody());
        verify(profileGameService).getAllGamesByProfileId(profileId);
    }
}