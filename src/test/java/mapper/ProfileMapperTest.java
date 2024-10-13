//package mapper;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.example.dto.ProfileDto;
//import org.example.model.Profile;
//import org.example.model.User;
//import org.example.mapper.ProfileMapper;
//import org.junit.jupiter.api.Test;
//
//class ProfileMapperTest {
//    private final ProfileMapper profileMapper = ProfileMapper.INSTANCE;
//
//    @Test
//    void testToDto() {
//        User user = new User();
//        user.setId(1);
//
//        Profile profile = new Profile();
//        profile.setId(1);
//        profile.setUserId(user);
//        profile.setFirstName("John");
//        profile.setLastName("Doe");
//        profile.setEmail("john.doe@example.com");
//
//        ProfileDto profileDto = profileMapper.toDto(profile);
//
//        assertNotNull(profileDto);
//        assertEquals(profile.getId(), profileDto.getId());
//        assertEquals(profile.getFirstName(), profileDto.getFirstName());
//        assertEquals(profile.getLastName(), profileDto.getLastName());
//        assertEquals(profile.getEmail(), profileDto.getEmail());
//        assertEquals(profile.getUserId().getId(), profileDto.getUserId());
//    }
//
//    @Test
//    void testToEntity() {
//        ProfileDto profileDto = new ProfileDto();
//        profileDto.setId(1);
//        profileDto.setUserId(1);
//        profileDto.setFirstName("John");
//        profileDto.setLastName("Doe");
//        profileDto.setEmail("john.doe@example.com");
//
//        Profile profile = profileMapper.toEntity(profileDto);
//
//        assertNotNull(profile);
//        assertEquals(profileDto.getId(), profile.getId());
//        assertEquals(profileDto.getFirstName(), profile.getFirstName());
//        assertEquals(profileDto.getLastName(), profile.getLastName());
//        assertEquals(profileDto.getEmail(), profile.getEmail());
//        assertNotNull(profile.getUserId());
//        assertEquals(profileDto.getUserId(), profile.getUserId().getId());
//    }
//
//    @Test
//    void testToDto_NullProfile() {
//        ProfileDto profileDto = profileMapper.toDto(null);
//
//        assertNull(profileDto);
//    }
//
//    @Test
//    void testToEntity_NullProfileDto() {
//        Profile profile = profileMapper.toEntity(null);
//
//        assertNull(profile);
//    }
//
//    @Test
//    void testToDto_NullUserId() {
//        Profile profile = new Profile();
//        profile.setId(2);
//        profile.setFirstName("John");
//        profile.setLastName("Doe");
//        profile.setEmail("john.doe@example.com");
//
//        ProfileDto profileDto = profileMapper.toDto(profile);
//
//        assertNotNull(profileDto);
//        assertEquals(profile.getId(), profileDto.getId());
//        assertNull(profileDto.getUserId());
//        assertEquals(profile.getFirstName(), profileDto.getFirstName());
//        assertEquals(profile.getLastName(), profileDto.getLastName());
//        assertEquals(profile.getEmail(), profileDto.getEmail());
//    }
//
//    @Test
//    void testToEntity_NullUserIdInDto() {
//        ProfileDto profileDto = new ProfileDto();
//        profileDto.setId(2);
//        profileDto.setFirstName("John");
//        profileDto.setLastName("Doe");
//        profileDto.setEmail("john.doe@example.com");
//
//        Profile profile = profileMapper.toEntity(profileDto);
//
//        assertNotNull(profile);
//        assertEquals(profileDto.getId(), profile.getId());
//
//        assertNotNull(profile.getUserId());
//        assertNull(profile.getUserId().getId());
//
//        assertEquals(profileDto.getFirstName(), profile.getFirstName());
//        assertEquals(profileDto.getLastName(), profile.getLastName());
//        assertEquals(profileDto.getEmail(), profile.getEmail());
//    }
//}
