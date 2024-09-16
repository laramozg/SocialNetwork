package org.example.dao;

import org.example.model.Post;
import org.example.model.Profile;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {
    private final DataSource dataSource;

    public PostDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Post> findByProfileId(int profileId) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM posts WHERE profile_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, profileId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                ProfileDao profileDao = new ProfileDao(dataSource);
                Profile profile = profileDao.findById(profileId);
                post.setProfileId(profile);

                post.setContent(rs.getString("content"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                posts.add(post);
            }
        }
        return posts;
    }

    public int save(Post post) throws SQLException {
        int postId = 0;
        String query = "INSERT INTO posts (profile_id, content, created_at) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, post.getProfileId().getId());
            ps.setString(2, post.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreatedAt()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                postId = rs.getInt("id");
                post.setId(postId);
            }
        }
        return postId;
    }


    public void delete(int id) throws SQLException {
        String query = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
