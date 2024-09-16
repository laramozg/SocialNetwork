package org.example.service;

import org.example.dao.PostDao;
import org.example.dto.PostDto;
import org.example.mapper.PostMapper;
import org.example.model.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class PostService {
    private final PostDao postDao;
    private final PostMapper postMapper = PostMapper.INSTANCE;

    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    public int savePost(PostDto postDto) throws SQLException {
        Post post = postMapper.toEntity(postDto);
        return postDao.save(post);
    }

    public List<PostDto> getPostsByProfileId(int profileId) throws SQLException {
        List<Post> posts = postDao.findByProfileId(profileId);
        return posts.stream().map(postMapper::toDto).collect(Collectors.toList());
    }

    public void deletePost(int id) throws SQLException {
        postDao.delete(id);
    }
}
