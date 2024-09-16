package mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.junit.jupiter.api.Test;

class UserMapperTest {
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void testToDto() {
        User user = new User();
        user.setId(1);
        user.setUsername("john_doe");
        user.setPassword("securepassword");

        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getPassword(), userDto.getPassword());
    }

    @Test
    void testToEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("john_doe");
        userDto.setPassword("securepassword");

        User user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    void testToDto_NullUser() {
        UserDto userDto = userMapper.toDto(null);

        assertNull(userDto);
    }

    @Test
    void testToEntity_NullUserDto() {
        User user = userMapper.toEntity(null);

        assertNull(user);
    }
}
