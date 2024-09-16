package service;

import org.example.dao.GameDao;
import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;
import org.example.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {
    @Mock
    private GameDao gameDao;

    @InjectMocks
    private GameService gameService;

    private GameMapper gameMapper;

    @BeforeEach
    void setUp()  {
        MockitoAnnotations.openMocks(this);
        gameMapper = GameMapper.INSTANCE;
    }

    @Test
    void testSaveGame() throws SQLException {
        GameDto gameDto = new GameDto(null, "Game Title", "Game Description");
        Game game = gameMapper.toEntity(gameDto);

        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);
        when(gameDao.save(any(Game.class))).thenReturn(1);

        int result = gameService.saveGame(gameDto);

        assertEquals(1, result);
        verify(gameDao, times(1)).save(gameCaptor.capture());

        Game capturedGame = gameCaptor.getValue();
        assertEquals(game.getTitle(), capturedGame.getTitle());
        assertEquals(game.getGenre(), capturedGame.getGenre());
    }

    @Test
    void testGetGameById() throws SQLException {
        Game game = new Game();
        game.setId(1);
        game.setTitle("Game Title");
        game.setGenre("Game Description");

        GameDto gameDto = new GameDto(1, "Game Title", "Game Description");

        when(gameDao.findById(1)).thenReturn(game);
        GameDto result = gameService.getGameById(1);

        assertEquals(gameDto.getTitle(), result.getTitle());
        assertEquals(gameDto.getGenre(), result.getGenre());
    }

    @Test
    void testGetAllGames() throws SQLException {
        Game game1 = new Game();
        game1.setId(1);
        game1.setTitle("Game 1");
        game1.setGenre("Description 1");

        Game game2 = new Game();
        game2.setId(2);
        game2.setTitle("Game 2");
        game2.setGenre("Description 2");

        GameDto gameDto1 = new GameDto(1, "Game 1", "Description 1");
        GameDto gameDto2 = new GameDto(2, "Game 2", "Description 2");

        List<Game> games = Arrays.asList(game1, game2);
        List<GameDto> gameDtos = Arrays.asList(gameDto1, gameDto2);

        when(gameDao.findAll()).thenReturn(games);
        List<GameDto> result = gameService.getAllGames();

        assertEquals(gameDtos.size(), result.size());
        assertEquals(gameDtos.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(gameDtos.get(1).getTitle(), result.get(1).getTitle());
    }
}
