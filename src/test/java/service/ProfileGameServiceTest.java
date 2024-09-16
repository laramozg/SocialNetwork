package service;

import org.example.dao.ProfileGameDao;
import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;
import org.example.service.ProfileGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProfileGameServiceTest {
    @Mock
    private ProfileGameDao profileGameDao;

    private GameMapper gameMapper;

    @InjectMocks
    private ProfileGameService profileGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameMapper = GameMapper.INSTANCE;
    }

    @Test
    void testAddProfileToGame() throws SQLException {
        profileGameService.addProfileToGame(1, 1);
        verify(profileGameDao, times(1)).addProfileToGame(1, 1);
    }

    @Test
    void testGetGamesByProfileId() throws SQLException {
        Game game1 = new Game();
        game1.setId(1);
        game1.setTitle("Game 1");
        game1.setGenre("Description 1");

        Game game2 = new Game();
        game2.setId(2);
        game2.setTitle("Game 2");
        game2.setGenre("Description 2");

        GameDto gameDto1 = gameMapper.toDto(game1);
        GameDto gameDto2 =gameMapper.toDto(game2);

        List<Game> games = Arrays.asList(game1, game2);
        List<GameDto> gameDtos = Arrays.asList(gameDto1, gameDto2);


        when(profileGameDao.findGamesByProfileId(1)).thenReturn(games);

        List<GameDto> result = profileGameService.getGamesByProfileId(1);

        assertEquals(gameDtos.size(), result.size());
        assertEquals(gameDtos.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(gameDtos.get(1).getTitle(), result.get(1).getTitle());
    }
}
