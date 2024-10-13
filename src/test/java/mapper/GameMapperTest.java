package mapper;

import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMapperTest {
    private final GameMapper gameMapper = GameMapper.INSTANCE;

    @Test
    void testToDto() {
        Game game = new Game();
        game.setId(1);
        game.setTitle("The Legend of Zelda");
        game.setGenre("Action-Adventure");

        GameDto gameDto = gameMapper.toDto(game);

        assertNotNull(gameDto);
        assertEquals(game.getId(), gameDto.getId());
        assertEquals(game.getTitle(), gameDto.getTitle());
        assertEquals(game.getGenre(), gameDto.getGenre());
    }

    @Test
    void testToEntity() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1);
        gameDto.setTitle("The Legend of Zelda");
        gameDto.setGenre("Action-Adventure");

        Game game = gameMapper.toEntity(gameDto);

        assertNotNull(game);
        assertEquals(gameDto.getId(), game.getId());
        assertEquals(gameDto.getTitle(), game.getTitle());
        assertEquals(gameDto.getGenre(), game.getGenre());
    }

    @Test
    void testToDto_NullGame() {
        GameDto gameDto = gameMapper.toDto(null);

        assertNull(gameDto);
    }

    @Test
    void testToEntity_NullGameDto() {
        Game game = gameMapper.toEntity(null);

        assertNull(game);
    }
}
