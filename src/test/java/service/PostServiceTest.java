package service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.PostDto;
import org.example.mapper.PostMapper;
import org.example.model.Post;
import org.example.model.Profile;
import org.example.repository.PostRepository;
import org.example.repository.ProfileRepository;
import org.example.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private PostService postService;

    private PostMapper postMapper = PostMapper.INSTANCE;

    private Profile profile;
    private Post post;
    private PostDto postDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        LocalDateTime now = LocalDateTime.now();
        profile = new Profile();
        profile.setId(1);

        post = new Post();
        post.setId(1);
        post.setProfileId(profile);
        post.setContent("Test Post");
        post.setCreatedAt(now);

        postDto = new PostDto();
        postDto.setId(1);
        postDto.setProfileId(1);
        postDto.setContent("Test Post");
        postDto.setContent(now.toString());
    }

    @Test
    void testCreatePost_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto result = postService.createPost(postDto);

        assertEquals("Test Post", result.getContent());
        verify(profileRepository, times(1)).findById(1);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testCreatePost_ProfileNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.createPost(postDto));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void testGetPostsByProfileId_Success() {
        when(profileRepository.findById(1)).thenReturn(Optional.of(profile));
        when(postRepository.findByProfileId(profile)).thenReturn(List.of(post));

        List<PostDto> result = postService.getPostsByProfileId(1);

        assertEquals(1, result.size());
        assertEquals("Test Post", result.get(0).getContent());
        verify(profileRepository, times(1)).findById(1);
        verify(postRepository, times(1)).findByProfileId(profile);
    }

    @Test
    void testGetPostsByProfileId_ProfileNotFound() {
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.getPostsByProfileId(1));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository, times(1)).findById(1);
        verify(postRepository, never()).findByProfileId(any(Profile.class));
    }

    @Test
    void testDeletePostById_Success() {
        when(postRepository.existsById(1)).thenReturn(true);

        postService.deletePostById(1);

        verify(postRepository, times(1)).existsById(1);
        verify(postRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletePostById_PostNotFound() {
        when(postRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> postService.deletePostById(1));

        assertEquals("Post not found", exception.getMessage());
        verify(postRepository, times(1)).existsById(1);
        verify(postRepository, never()).deleteById(1);
    }
}
