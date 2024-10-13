package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.GameDto;
import org.example.mapper.GameMapper;
import org.example.model.Game;
import org.example.model.Profile;
import org.example.model.ProfileGame;
import org.example.model.ProfileGameId;
import org.example.repository.GameRepository;
import org.example.repository.ProfileGameRepository;
import org.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileGameService {
    private final ProfileGameRepository profileGameRepository;
    private final ProfileRepository profileRepository;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper = GameMapper.INSTANCE;

    @Autowired
    public ProfileGameService(ProfileGameRepository profileGameRepository, ProfileRepository profileRepository,
                              GameRepository gameRepository) {
        this.profileGameRepository = profileGameRepository;
        this.profileRepository = profileRepository;
        this.gameRepository = gameRepository;
    }

    public void addProfileToGame(Integer profileId, Integer gameId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        ProfileGameId profileGameId = new ProfileGameId(profileId, gameId);
        ProfileGame profileGame = new ProfileGame();
        profileGame.setProfileId(profile);
        profileGame.setGameId(game);
        profileGame.setId(profileGameId);
        profileGameRepository.save(profileGame);
    }


    public List<GameDto> getAllGamesByProfileId(Integer profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        List<ProfileGame> profileGames = profileGameRepository.findAllByProfileId(profile);
        return profileGames.stream()
                .map(ProfileGame::getGameId)
                .map(gameMapper::toDto)
                .toList();
    }
}
