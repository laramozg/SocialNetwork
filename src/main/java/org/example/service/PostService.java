package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.PostDto;
import org.example.mapper.PostMapper;
import org.example.model.Post;
import org.example.model.Profile;
import org.example.repository.PostRepository;
import org.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final PostMapper postMapper = PostMapper.INSTANCE;

    @Autowired
    public PostService(PostRepository postRepository, ProfileRepository profileRepository) {
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
    }

    public PostDto createPost(PostDto postDto) {
        profileRepository.findById(postDto.getProfileId())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        Post post = postMapper.toEntity(postDto);
        return postMapper.toDto(postRepository.save(post));
    }


    public List<PostDto> getPostsByProfileId(Integer profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        List<Post> posts = postRepository.findByProfileId(profile);
        return posts.stream()
                .map(postMapper::toDto)
                .toList();
    }

    public void deletePostById(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new EntityNotFoundException("Post not found");
        }
        postRepository.deleteById(id);
    }
}
