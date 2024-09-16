package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.controller.PostServlet;
import org.example.dto.PostDto;
import org.example.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServletTest {
    @Mock
    private PostService postService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private PostServlet postServlet;

    private ObjectMapper objectMapper;

    @Mock
    PrintWriter writer;


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoPost_Success() throws Exception {
        PostDto postDto = new PostDto(null, 1, "Content of the post", LocalDateTime.now());
        String postDtoJson = objectMapper.writeValueAsString(postDto);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(postDtoJson.getBytes())));

        when(request.getReader()).thenReturn(reader);
        when(postService.savePost(any(PostDto.class))).thenReturn(1);

        postServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).write("Post created with ID: 1");
    }

    @Test
    void testDoGet_Success() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");

        List<PostDto> posts = Arrays.asList(
                new PostDto(1, 1, "Content 1", LocalDateTime.now()),
                new PostDto(2, 1, "Content 2", LocalDateTime.now())
        );
        when(postService.getPostsByProfileId(anyInt())).thenReturn(posts);

        postServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String expectedJsonResponse = objectMapper.writeValueAsString(posts);
        verify(writer).write(expectedJsonResponse);
    }

    @Test
    void testDoDelete_Success() throws Exception {
        when(request.getPathInfo()).thenReturn("/1");

        postServlet.doDelete(request, response);

        verify(postService, times(1)).deletePost(1);
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoPost_Exception() throws Exception {
        PostDto postDto = new PostDto(null, 1, "Content of the post", LocalDateTime.now());
        String postDtoJson = objectMapper.writeValueAsString(postDto);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(postDtoJson.getBytes())));

        when(request.getReader()).thenReturn(reader);
        when(postService.savePost(any(PostDto.class))).thenThrow(new SQLException("Database error"));

        postServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(writer).write("Error saving post.");
    }

    @Test
    void testDoGet_InvalidProfileId() throws Exception {
        when(request.getPathInfo()).thenReturn("/invalid");

        postServlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("Invalid Profile ID.");
    }

    @Test
    void testDoDelete_InvalidPostId() {
        when(request.getPathInfo()).thenReturn("/invalid");

        postServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("Invalid Post ID.");
    }
}
