package org.example.dao;

import org.example.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User findById(Integer id) throws SQLException {
        User user = null;
        String query = "SELECT * FROM users u WHERE u.id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = mapRowToUser(rs);
            }
        }
        return user;
    }

    public int save(User user) throws SQLException {
        int userId = 0;
        String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertUserQuery)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
                user.setId(userId);
            }
        }
        return userId;
    }

    public void update(User user) throws SQLException {
        String updateUserQuery = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateUserQuery)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteById(Integer id) throws SQLException {
        String deleteUserQuery = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteUserQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));

        return user;
    }
}
