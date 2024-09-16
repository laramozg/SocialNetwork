package service;

import org.example.dao.PostDao;
import org.example.dto.PostDto;
import org.example.mapper.PostMapper;
import org.example.model.Post;
import org.example.model.Profile;
import org.example.model.User;
import org.example.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @Mock
    private PostDao postDao;

    @InjectMocks
    private PostService postService;

    private PostMapper postMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postMapper = PostMapper.INSTANCE;
    }

    @Test
    void testSavePost() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        PostDto postDto = new PostDto(null, 1, "Content", now);
        Post post = new Post();
        post.setContent("Content");
        post.setCreatedAt(now);
        post.setProfileId(new Profile(1, "John", "Doe", "john.doe@example.com", new User(1, "newuser", "password")));

        when(postDao.save(any(Post.class))).thenReturn(1);

        int result = postService.savePost(postDto);

        assertEquals(1, result);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postDao, times(1)).save(postCaptor.capture());

        Post capturedPost = postCaptor.getValue();
        assertEquals(post.getContent(), capturedPost.getContent());
        assertEquals(post.getCreatedAt(), capturedPost.getCreatedAt());
        assertEquals(post.getProfileId().getId(), capturedPost.getProfileId().getId());
    }

    @Test
    void testGetPostsByProfileId() throws SQLException {
        LocalDateTime now = LocalDateTime.now();

        Post post = new Post();
        post.setId(1);
        post.setContent("Content");
        post.setCreatedAt(now);
        post.setProfileId(new Profile(1, "John", "Doe", "john.doe@example.com", new User(1, "newuser", "password")));

        PostDto postDto = postMapper.toDto(post);

        List<Post> posts = new ArrayList<>();
        posts.add(post);

        when(postDao.findByProfileId(1)).thenReturn(posts);

        List<PostDto> result = postService.getPostsByProfileId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(postDto.getContent(), result.get(0).getContent());
        assertEquals(postDto.getProfileId(), result.get(0).getProfileId());
        verify(postDao, times(1)).findByProfileId(1);

    }

    @Test
    void testDeletePost() throws SQLException {
        int postId = 1;
        postService.deletePost(postId);
        verify(postDao, times(1)).delete(postId);
    }

}
