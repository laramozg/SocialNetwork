package service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.ProfileDto;
import org.example.mapper.ProfileMapper;
import org.example.model.Profile;
import org.example.model.User;
import org.example.repository.ProfileRepository;
import org.example.repository.UserRepository;
import org.example.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileService profileService;

    private ProfileMapper profileMapper = ProfileMapper.INSTANCE;

    private Profile profile;
    private ProfileDto profileDto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        profile = new Profile();
        profile.setId(1);
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setEmail("john.doe@example.com");
        profile.setUserId(user);

        profileDto = new ProfileDto();
        profileDto.setId(1);
        profileDto.setFirstName("John");
        profileDto.setLastName("Doe");
        profileDto.setEmail("john.doe@example.com");
    }

    @Test
    void testGetProfileByUserId_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(user)).thenReturn(Optional.of(profile));

        ProfileDto result = profileService.getProfileByUserId(1);

        assertEquals(profileDto.getFirstName(), result.getFirstName());
        verify(userRepository, times(1)).findById(1);
        verify(profileRepository, times(1)).findByUserId(user);
    }

    @Test
    void testGetProfileByUserId_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileService.getProfileByUserId(1));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(profileRepository, never()).findByUserId(any());
    }

    @Test
    void testGetProfileByUserId_ProfileNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(user)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileService.getProfileByUserId(1));

        assertEquals("Profile not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(profileRepository, times(1)).findByUserId(user);
    }

    @Test
    void testCreateProfile() {
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileDto result = profileService.createProfile(profileDto);

        assertEquals(profileDto.getFirstName(), result.getFirstName());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testFindById_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        ProfileDto result = profileService.findById(1);

        assertEquals(profileDto.getFirstName(), result.getFirstName());
        verify(profileRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileService.findById(1));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateProfile_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        profileDto.setFirstName("UpdatedFirstName");
        ProfileDto result = profileService.updateProfile(1, profileDto);

        assertEquals("UpdatedFirstName", result.getFirstName());
        verify(profileRepository, times(1)).findById(1);
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void testUpdateProfile_NotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileService.updateProfile(1, profileDto));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void testDeleteProfile_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        profileService.deleteProfile(1);

        verify(profileRepository, times(1)).findById(1);
        verify(profileRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteProfile_NotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> profileService.deleteProfile(1));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
        verify(profileRepository, never()).deleteById(1);
    }
}