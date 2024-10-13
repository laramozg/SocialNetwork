package service;

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
import org.example.service.ProfileGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfileGameServiceTest {
    @Mock
    private ProfileGameRepository profileGameRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private ProfileGameService profileGameService;

    private Profile profile;
    private Game game;
    private ProfileGame profileGame;
    private GameMapper gameMapper = GameMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        profile = new Profile();
        profile.setId(1);

        game = new Game();
        game.setId(1);

        profileGame = new ProfileGame();
        profileGame.setProfileId(profile);
        profileGame.setGameId(game);
        profileGame.setId(new ProfileGameId(1, 1));
    }

    @Test
    void testAddProfileToGame_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        profileGameService.addProfileToGame(1, 1);

        verify(profileRepository, times(1)).findById(1);
        verify(gameRepository, times(1)).findById(1);
        verify(profileGameRepository, times(1)).save(any(ProfileGame.class));
    }

    @Test
    void testAddProfileToGame_ProfileNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileGameService.addProfileToGame(1, 1));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
        verify(gameRepository, never()).findById(1);
        verify(profileGameRepository, never()).save(any(ProfileGame.class));
    }

    @Test
    void testAddProfileToGame_GameNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileGameService.addProfileToGame(1, 1));

        assertEquals("Game not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
        verify(gameRepository, times(1)).findById(1);
        verify(profileGameRepository, never()).save(any(ProfileGame.class));
    }

    @Test
    void testGetAllGamesByProfileId_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(profileGameRepository.findAllByProfileId(profile)).thenReturn(List.of(profileGame));
        when(gameRepository.findById(1)).thenReturn(Optional.of(game));

        List<GameDto> result = profileGameService.getAllGamesByProfileId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        verify(profileRepository, times(1)).findById(1);
        verify(profileGameRepository, times(1)).findAllByProfileId(profile);
    }

    @Test
    void testGetAllGamesByProfileId_ProfileNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileGameService.getAllGamesByProfileId(1));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
        verify(profileGameRepository, never()).findAllByProfileId(any(Profile.class));
    }
}
