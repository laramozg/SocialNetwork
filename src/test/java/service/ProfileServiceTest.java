package service;

import org.example.dao.ProfileDao;
import org.example.dto.ProfileDto;
import org.example.mapper.ProfileMapper;
import org.example.model.Profile;
import org.example.model.User;
import org.example.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceTest {
    @Mock
    private ProfileDao profileDao;

    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        profileMapper = ProfileMapper.INSTANCE;
    }

    @Test
    void testGetProfileByUserId() throws SQLException {
        User user = new User(1, "testuser", "password");

        Profile profile = new Profile();
        profile.setId(1);
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setEmail("john.doe@example.com");
        profile.setUserId(user);

        ProfileDto profileDto = profileMapper.toDto(profile);

        when(profileDao.findByUserId(1)).thenReturn(profile);

        ProfileDto result = profileService.getProfileByUserId(1);

        assertNotNull(result);
        assertEquals(profileDto.getFirstName(), result.getFirstName());
        assertEquals(profileDto.getLastName(), result.getLastName());
        assertEquals(profileDto.getEmail(), result.getEmail());
        verify(profileDao, times(1)).findByUserId(1);
    }

    @Test
    void testSaveProfile() throws SQLException {
        ProfileDto profileDto = new ProfileDto(null, "John", "Doe", "john.doe@example.com", 1);
        Profile profile = new Profile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setEmail("john.doe@example.com");
        profile.setUserId(new User(1, "newuser", "password"));

        when(profileDao.save(any(Profile.class))).thenReturn(1);

        int result = profileService.saveProfile(profileDto);

        assertEquals(1, result);

        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(profileDao, times(1)).save(profileCaptor.capture());

        Profile capturedProfile = profileCaptor.getValue();
        assertEquals(profile.getFirstName(), capturedProfile.getFirstName());
        assertEquals(profile.getLastName(), capturedProfile.getLastName());
        assertEquals(profile.getEmail(), capturedProfile.getEmail());
        assertEquals(profile.getUserId().getId(), capturedProfile.getUserId().getId());
    }

    @Test
    void testUpdateProfile() throws SQLException {
        ProfileDto profileDto = new ProfileDto(1, "John", "Doe", "john.doe@example.com", 1);
        Profile profile = profileMapper.toEntity(profileDto);

        profileService.updateProfile(profileDto);

        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(profileDao, times(1)).update(profileCaptor.capture());

        Profile capturedProfile = profileCaptor.getValue();

        assertEquals(profile.getId(), capturedProfile.getId());
        assertEquals(profile.getFirstName(), capturedProfile.getFirstName());
        assertEquals(profile.getLastName(), capturedProfile.getLastName());
        assertEquals(profile.getEmail(), capturedProfile.getEmail());
    }

    @Test
    void testDeleteProfile() throws SQLException {
        Integer userId = 1;
        profileService.deleteProfile(userId);
        verify(profileDao, times(1)).deleteByUserId(userId);
    }
}
