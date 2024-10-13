package controller;

import org.example.controller.ProfileController;
import org.example.dto.ProfileDto;
import org.example.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private ProfileDto profileDto;

    @BeforeEach
    public void setUp() {
        profileDto = new ProfileDto();
        profileDto.setId(1);
        profileDto.setUserId(1);
        profileDto.setFirstName("name");
        profileDto.setLastName("firstName");
        profileDto.setEmail("@gmail.com");
    }

    @Test
    void testCreateProfile() {
        when(profileService.createProfile(profileDto)).thenReturn(profileDto);

        ResponseEntity<ProfileDto> response = profileController.createProfile(profileDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(profileDto, response.getBody());
        verify(profileService).createProfile(profileDto);
    }

    @Test
    void testFindById() {
        when(profileService.findById(1)).thenReturn(profileDto);

        ResponseEntity<ProfileDto> response = profileController.findById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDto, response.getBody());
        verify(profileService).findById(1);
    }

    @Test
    void testFindByUserId() {
        when(profileService.getProfileByUserId(1)).thenReturn(profileDto);

        ResponseEntity<ProfileDto> response = profileController.findByUserId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDto, response.getBody());
        verify(profileService).getProfileByUserId(1);
    }

    @Test
    void testUpdateProfile() {
        when(profileService.updateProfile(1, profileDto)).thenReturn(profileDto);

        ResponseEntity<ProfileDto> response = profileController.updateProfile(1, profileDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profileDto, response.getBody());
        verify(profileService).updateProfile(1, profileDto);
    }

    @Test
    void testDeleteProfile() {
        doNothing().when(profileService).deleteProfile(1);

        ResponseEntity<Void> response = profileController.deleteProfile(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(profileService).deleteProfile(1);
    }
}
