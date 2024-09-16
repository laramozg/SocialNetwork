package org.example.service;

import org.example.dao.GameDao;
import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class GameService {
    private final GameDao gameDao;
    private final GameMapper gameMapper = GameMapper.INSTANCE;

    public GameService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public int saveGame(GameDto gameDto) throws SQLException {
        Game game = gameMapper.toEntity(gameDto);
        return gameDao.save(game);
    }

    public GameDto getGameById(int id) throws SQLException {
        Game game = gameDao.findById(id);
        return gameMapper.toDto(game);
    }

    public List<GameDto> getAllGames() throws SQLException {
        List<Game> games = gameDao.findAll();
        return games.stream().map(gameMapper::toDto).collect(Collectors.toList());
    }
}
