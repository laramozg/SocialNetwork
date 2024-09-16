package service;

import org.example.dao.UserDao;
import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private UserMapper userMapper ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userMapper = UserMapper.INSTANCE;
    }

    @Test
    void testGetUserById() throws SQLException {
        User user = new User(1, "testuser", "password");
        UserDto expectedUserDto = userMapper.toDto(user);

        when(userDao.findById(1)).thenReturn(user);

        UserDto result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(expectedUserDto.getUsername(), result.getUsername());
        assertEquals(expectedUserDto.getPassword(), result.getPassword());
        verify(userDao, times(1)).findById(1);
    }

    @Test
    void testSaveUser() throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setUsername("newuser");
        userDto.setPassword("password");

        User user = userMapper.toEntity(userDto);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userDao.save(any(User.class))).thenReturn(1);

        int result = userService.saveUser(userDto);

        assertEquals(1, result);
        verify(userDao, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.getPassword(), capturedUser.getPassword());
    }

    @Test
    void testUpdateUser() throws SQLException {
        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("updateduser");
        userDto.setPassword("newpassword");

        User expectedUser = userMapper.toEntity(userDto);

        userService.updateUser(userDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao, times(1)).update(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertEquals(expectedUser.getId(), capturedUser.getId());
        assertEquals(expectedUser.getUsername(), capturedUser.getUsername());
        assertEquals(expectedUser.getPassword(), capturedUser.getPassword());
    }

    @Test
    void testDeleteUser() throws SQLException {
        userService.deleteUser(1);
        verify(userDao, times(1)).deleteById(1);
    }

}

