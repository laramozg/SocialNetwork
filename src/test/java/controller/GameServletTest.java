package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.GameServlet;
import org.example.dto.GameDto;
import org.example.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GameServletTest {
    @Mock
    private GameService gameService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private GameServlet gameServlet;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDoGet_AllGames() throws Exception {
        List<GameDto> games = Arrays.asList(new GameDto(1, "Game 1", "Description 1"),
                new GameDto(2, "Game 2", "Description 2"));
        StringWriter stringWriter =  new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        when(gameService.getAllGames()).thenReturn(games);

        when(request.getPathInfo()).thenReturn(null);

        gameServlet.doGet(request, response);

        verify(gameService, times(1)).getAllGames();
        writer.flush();
        String jsonResponse = stringWriter.toString();
        assertEquals(objectMapper.writeValueAsString(games), jsonResponse);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoGet_GameById() throws Exception {
        GameDto gameDto = new GameDto(1, "Game 1", "Description 1");
        StringWriter stringWriter =  new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        when(gameService.getGameById(1)).thenReturn(gameDto);

        when(request.getPathInfo()).thenReturn("/1");

        gameServlet.doGet(request, response);

        verify(gameService, times(1)).getGameById(1);
        writer.flush();
        String jsonResponse = stringWriter.toString();
        assertEquals(objectMapper.writeValueAsString(gameDto), jsonResponse);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost_Success() throws Exception {
        GameDto gameDto = new GameDto(1, "New Game", "New Description");
        String gameDtoJson = objectMapper.writeValueAsString(gameDto);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gameDtoJson.getBytes());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        when(request.getReader()).thenReturn(bufferedReader);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        when(gameService.saveGame(any(GameDto.class))).thenReturn(1);
        gameServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write("Game created with ID: 1");
    }

    @Test
    void testDoPost_InternalServerError() throws Exception {
        GameDto gameDto = new GameDto(1, "New Game", "New Description");
        String gameDtoJson = objectMapper.writeValueAsString(gameDto);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gameDtoJson.getBytes());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        when(request.getReader()).thenReturn(bufferedReader);

        when(gameService.saveGame(any(GameDto.class))).thenThrow(new SQLException("Database error"));

        gameServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}

