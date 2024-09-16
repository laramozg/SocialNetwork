package mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.example.dto.PostDto;
import org.example.model.Post;
import org.example.model.Profile;
import org.example.mapper.PostMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class PostMapperTest {
    private final PostMapper postMapper = PostMapper.INSTANCE;

    @Test
    void testToDto() {
        Profile profile = new Profile();
        profile.setId(1);

        Post post = new Post();
        post.setId(1);
        post.setProfileId(profile);
        post.setContent("This is a post.");
        post.setCreatedAt(java.time.LocalDateTime.now());

        PostDto postDto = postMapper.toDto(post);

        assertNotNull(postDto);
        assertEquals(post.getId(), postDto.getId());
        assertEquals(post.getContent(), postDto.getContent());
        assertEquals(post.getCreatedAt(), postDto.getCreatedAt());
        assertEquals(post.getProfileId().getId(), postDto.getProfileId());
    }

    @Test
    void testToEntity() {
        PostDto postDto = new PostDto();
        postDto.setId(1);
        postDto.setProfileId(1);
        postDto.setContent("This is a post.");
        postDto.setCreatedAt(java.time.LocalDateTime.now());

        Post post = postMapper.toEntity(postDto);

        assertNotNull(post);
        assertEquals(postDto.getId(), post.getId());
        assertEquals(postDto.getContent(), post.getContent());
        assertEquals(postDto.getCreatedAt(), post.getCreatedAt());
        assertNotNull(post.getProfileId());
        assertEquals(postDto.getProfileId(), post.getProfileId().getId());
    }

    @Test
    void testToDto_NullPost() {
        PostDto postDto = postMapper.toDto(null);

        assertNull(postDto);
    }

    @Test
    void testToEntity_NullPostDto() {
        Post post = postMapper.toEntity(null);

        assertNull(post);
    }
    @Test
    void testToDto_NullProfile() {
        Post post = new Post();
        post.setId(2);
        post.setContent("Test Content");
        post.setCreatedAt(LocalDateTime.now());

        PostDto postDto = postMapper.toDto(post);

        assertNotNull(postDto);
        assertEquals(post.getId(), postDto.getId());
        assertEquals(0, postDto.getProfileId());
        assertEquals(post.getContent(), postDto.getContent());
        assertEquals(post.getCreatedAt(), postDto.getCreatedAt());
    }


    @Test
    void testToEntity_NullProfileIdInDto() {
        PostDto postDto = new PostDto();
        postDto.setId(1);
        postDto.setContent("Test content");
        postDto.setCreatedAt(LocalDateTime.now());

        Post post = postMapper.toEntity(postDto);

        assertNotNull(post);
        assertEquals(postDto.getId(), post.getId());
        assertNotNull(post.getProfileId());

        assertEquals(postDto.getContent(), post.getContent());
        assertEquals(postDto.getCreatedAt(), post.getCreatedAt());
    }
}
