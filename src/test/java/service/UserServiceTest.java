package service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserMapper userMapper = UserMapper.INSTANCE;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password");

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testuser");
        userDto.setPassword("password");
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1);

        assertEquals(userDto.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1));

        assertEquals("User with username '1' not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.saveUser(userDto);

        assertEquals(userDto.getUsername(), result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userDto.setUsername("updatedUser");
        UserDto result = userService.updateUser(1, userDto);

        assertEquals("updatedUser", result.getUsername());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1, userDto));

        assertEquals("User with username '1' not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1));

        assertEquals("User with username '1' not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, never()).deleteById(1);
    }
}

