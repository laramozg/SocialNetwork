package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;
import org.example.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper = GameMapper.INSTANCE;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameDto> findAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(gameMapper::toDto)
                .toList();
    }

    public GameDto findGameById(Integer id) {
        return gameRepository.findById(id)
                .map(gameMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));
    }

    public GameDto createGame(GameDto gameDto) {
        Game game = gameMapper.toEntity(gameDto);
        return gameMapper.toDto(gameRepository.save(game));
    }
}
