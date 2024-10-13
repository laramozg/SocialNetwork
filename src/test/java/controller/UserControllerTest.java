package controller;

import org.example.controller.UserController;
import org.example.dto.UserDto;
import org.example.service.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testuser");
        userDto.setPassword("testpass");
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService).getUserById(1);
    }

    @Test
    void testCreateUser() {
        when(userService.saveUser(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService).saveUser(userDto);
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(1, userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.updateUser(1, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService).updateUser(1, userDto);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser(1);

        ResponseEntity<Void> response = userController.deleteUser(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1);
    }
}