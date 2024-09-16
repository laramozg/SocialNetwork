package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.UserServlet;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServletTest {

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserServlet userServlet;


    private ByteArrayOutputStream outputStream;
    private PrintWriter writer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
    }

    @Test
    void testDoGet_UserFound() throws Exception {
        UserDto userDto = new UserDto(1, "John Doe", "JohnJohn");
        when(userService.getUserById(1)).thenReturn(userDto);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        writer.flush();
        String result = outputStream.toString();
        assertEquals(new ObjectMapper().writeValueAsString(userDto), result);
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_UserNotFound() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(null);
        when(request.getPathInfo()).thenReturn("/1");

        when(response.getWriter()).thenReturn(writer);

        userServlet.doGet(request, response);

        writer.flush();
        String result = outputStream.toString();
        assertEquals("User not found.", result);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoPost_Success() throws Exception {
        UserDto userDto = new UserDto(1, "New User", "NewPassword");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        BufferedReader bufferedReader = new BufferedReader(new StringReader(userDtoJson));
        when(userService.saveUser(any(UserDto.class))).thenReturn(1);

        when(request.getReader()).thenReturn(bufferedReader);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        writer.flush();
        String result = outputStream.toString();
        assertEquals("User created with ID: 1", result);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPost_Error() throws Exception {
        UserDto userDto = new UserDto(1, "New User", "NewPassword");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        BufferedReader bufferedReader = new BufferedReader(new StringReader(userDtoJson));
        when(userService.saveUser(any(UserDto.class))).thenThrow(new SQLException("Database error"));

        when(request.getReader()).thenReturn(bufferedReader);

        when(response.getWriter()).thenReturn(writer);

        userServlet.doPost(request, response);

        writer.flush();
        String result = outputStream.toString();
        assertEquals("Error creating user.", result);
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void testDoPut_Success() throws Exception {
        UserDto userDto = new UserDto(1, "Updated User", "UpdatedPassword");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        BufferedReader bufferedReader = new BufferedReader(new StringReader(userDtoJson));
        doNothing().when(userService).updateUser(any(UserDto.class));

        when(request.getReader()).thenReturn(bufferedReader);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPut(request, response);

        writer.flush();
        String result = outputStream.toString();
        assertEquals("User updated.", result);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPut_Error() throws Exception {
        UserDto userDto = new UserDto(1, "Updated User", "UpdatedPassword");
        String userDtoJson = new ObjectMapper().writeValueAsString(userDto);

        BufferedReader bufferedReader = new BufferedReader(new StringReader(userDtoJson));
        doThrow(new SQLException("Database error")).when(userService).updateUser(any(UserDto.class));

        when(request.getReader()).thenReturn(bufferedReader);
        when(response.getWriter()).thenReturn(writer);

        userServlet.doPut(request, response);

        writer.flush();
        String result = outputStream.toString();
        assertEquals("Error updating user.", result);
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void testDoDelete_Success() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");

        when(response.getWriter()).thenReturn(writer);

        doNothing().when(userService).deleteUser(anyInt());

        userServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDelete_Error() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");

        when(response.getWriter()).thenReturn(writer);

        doThrow(new SQLException("Database error")).when(userService).deleteUser(anyInt());

        userServlet.doDelete(request, response);

        writer.flush();
        String result = outputStream.toString();
        assertEquals("Error deleting user.", result);
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}