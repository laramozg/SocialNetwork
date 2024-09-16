package org.example.service;

import org.example.dao.ProfileGameDao;
import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileGameService {
    private final ProfileGameDao profileGameDao;
    private final GameMapper gameMapper = GameMapper.INSTANCE;

    public ProfileGameService(ProfileGameDao profileGameDao) {
        this.profileGameDao = profileGameDao;
    }

    public void addProfileToGame(int profileId, int gameId) throws SQLException {
        profileGameDao.addProfileToGame(profileId, gameId);
    }

    public List<GameDto> getGamesByProfileId(int profileId) throws SQLException {
        List<Game> games = profileGameDao.findGamesByProfileId(profileId);
        return games.stream()
                .map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
}
