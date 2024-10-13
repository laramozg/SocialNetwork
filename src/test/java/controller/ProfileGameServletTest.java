//package controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.controller.ProfileGameServlet;
//import org.example.dto.GameDto;
//import org.example.service.ProfileGameService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//class ProfileGameServletTest {
//    @Mock
//    private ProfileGameService profileGameService;
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//
//    @InjectMocks
//    private ProfileGameServlet profileGameServlet;
//
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private PrintWriter writer;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        MockitoAnnotations.openMocks(this);
//        objectMapper = new ObjectMapper();
//        when(response.getWriter()).thenReturn(writer);
//
//    }
//
//    @Test
//    void testDoPost_Success() throws Exception {
//        when(request.getParameter("profileId")).thenReturn("1");
//        when(request.getParameter("gameId")).thenReturn("2");
//
//        doNothing().when(profileGameService).addProfileToGame(1, 2);
//
//        profileGameServlet.doPost(request, response);
//
//        verify(profileGameService).addProfileToGame(1, 2);
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(writer).write("Adding profile to game.");
//    }
//
//    @Test
//    void testDoPost_Failure_InvalidParameters() throws Exception {
//        when(request.getParameter("profileId")).thenReturn("invalid");
//        when(request.getParameter("gameId")).thenReturn("2");
//
//        profileGameServlet.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        verify(writer).write("Invalid profile or game ID.");
//    }
//
//    @Test
//    void testDoPost_SQLException() throws Exception {
//        when(request.getParameter("profileId")).thenReturn("1");
//        when(request.getParameter("gameId")).thenReturn("2");
//
//        doThrow(new SQLException()).when(profileGameService).addProfileToGame(1, 2);
//
//        profileGameServlet.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        verify(writer).write("Error adding profile to game.");
//    }
//
//    @Test
//    void testDoGet_Success() throws Exception {
//        when(request.getPathInfo()).thenReturn("/1");
//
//        List<GameDto> games = Arrays.asList(
//                new GameDto(1, "Game 1", "Description 1"),
//                new GameDto(2, "Game 1", "Description 1")
//        );
//
//        when(profileGameService.getGamesByProfileId(1)).thenReturn(games);
//
//        profileGameServlet.doGet(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//
//        String expectedJsonResponse = objectMapper.writeValueAsString(games);
//        verify(writer).write(expectedJsonResponse);
//    }
//
//    @Test
//    void testDoGet_ProfileIdMissing() throws Exception {
//        when(request.getPathInfo()).thenReturn("/");
//
//        profileGameServlet.doGet(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        verify(writer).write("Profile ID is missing.");
//    }
//
//    @Test
//    void testDoGet_SQLException() throws Exception {
//        when(request.getPathInfo()).thenReturn("/1");
//
//        when(profileGameService.getGamesByProfileId(1)).thenThrow(new SQLException());
//
//        profileGameServlet.doGet(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        verify(writer).write("Error retrieving games for profile.");
//    }
//}
//
