package controller;

import org.example.controller.GameController;
import org.example.dto.GameDto;
import org.example.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private GameDto gameDto;

    @BeforeEach
    public void setUp() {
        gameDto = new GameDto();
        gameDto.setId(1);
        gameDto.setTitle("Test Game");
        gameDto.setGenre("Test Genre");
    }

    @Test
    void testFindAllGames() {
        List<GameDto> games = Collections.singletonList(gameDto);
        when(gameService.findAllGames()).thenReturn(games);

        ResponseEntity<List<GameDto>> response = gameController.findAllGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(games, response.getBody());
        verify(gameService).findAllGames();
    }

    @Test
    void testFindGameById() {
        when(gameService.findGameById(1)).thenReturn(gameDto);

        ResponseEntity<GameDto> response = gameController.findGameById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameDto, response.getBody());
        verify(gameService).findGameById(1);
    }

    @Test
    void testCreateGame() {
        when(gameService.createGame(gameDto)).thenReturn(gameDto);

        ResponseEntity<GameDto> response = gameController.createGame(gameDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(gameDto, response.getBody());
        verify(gameService).createGame(gameDto);
    }
}