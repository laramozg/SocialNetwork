package service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;
import org.example.repository.GameRepository;
import org.example.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {
    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private GameMapper gameMapper = GameMapper.INSTANCE;

    private Game game;
    private GameDto gameDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        game = new Game();
        game.setId(1);
        game.setTitle("Test Game");
        game.setGenre("Test Genre");

        gameDto = new GameDto();
        gameDto.setId(1);
        gameDto.setTitle("Test Game");
        gameDto.setGenre("Test Genre");
    }

    @Test
    void testFindAllGames_Success() {
        when(gameRepository.findAll()).thenReturn(List.of(game));

        List<GameDto> result = gameService.findAllGames();

        assertEquals(1, result.size());
        assertEquals("Test Game", result.get(0).getTitle());
        assertEquals("Test Genre", result.get(0).getGenre());
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testFindGameById_Success() {
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        GameDto result = gameService.findGameById(1);

        assertEquals("Test Game", result.getTitle());
        assertEquals("Test Genre", result.getGenre());
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    void testFindGameById_NotFound() {
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> gameService.findGameById(1));

        assertEquals("Game not found", exception.getMessage());
        verify(gameRepository, times(1)).findById(1);
    }

    @Test
    void testCreateGame_Success() {
        when(gameRepository.save(any(Game.class))).thenReturn(game);

        GameDto result = gameService.createGame(gameDto);

        assertEquals("Test Game", result.getTitle());
        verify(gameRepository, times(1)).save(any(Game.class));
    }
}
