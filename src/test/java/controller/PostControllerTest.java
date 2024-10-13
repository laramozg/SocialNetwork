package controller;

import org.example.controller.PostController;
import org.example.dto.PostDto;
import org.example.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private PostDto postDto;

    @BeforeEach
    public void setUp() {
        postDto = new PostDto();
        postDto.setId(1);
        postDto.setProfileId(1);
        postDto.setContent("Test Post");
        postDto.setCreatedAt(LocalDate.now().toString());
    }

    @Test
    void testCreatePost() {
        when(postService.createPost(postDto)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.createPost(postDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postDto, response.getBody());
        verify(postService).createPost(postDto);
    }

    @Test
    void testGetAllPostsByProfileId() {
        List<PostDto> postList = new ArrayList<>();
        postList.add(postDto);
        when(postService.getPostsByProfileId(1)).thenReturn(postList);

        ResponseEntity<List<PostDto>> response = postController.getAllPostByProfileId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postList, response.getBody());
        verify(postService).getPostsByProfileId(1);
    }

    @Test
    void testDeletePost() {
        doNothing().when(postService).deletePostById(1);

        ResponseEntity<Void> response = postController.deletePost(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(postService).deletePostById(1);
    }
}