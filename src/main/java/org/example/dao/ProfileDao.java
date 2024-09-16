package org.example.dao;

import org.example.model.Profile;
import org.example.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDao {
    private final DataSource dataSource;

    public ProfileDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Profile findByUserId(Integer userId) throws SQLException {
        Profile profile = null;
        String query = "SELECT * FROM profiles WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                profile = mapRowToProfile(rs);
            }
        }
        return profile;
    }

    public int save(Profile profile) throws SQLException {
        int profileId = 0;
        String insertProfileQuery = "INSERT INTO profiles (first_name, last_name, email, user_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertProfileQuery)) {
            stmt.setString(1, profile.getFirstName());
            stmt.setString(2, profile.getLastName());
            stmt.setString(3, profile.getEmail());
            stmt.setInt(4, profile.getUserId().getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                profileId = rs.getInt("id");
                profile.setId(profileId);

            }
        }
        return profileId;
    }

    public void update(Profile profile) throws SQLException {
        String updateQuery = "UPDATE profiles SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, profile.getFirstName());
            stmt.setString(2, profile.getLastName());
            stmt.setString(3, profile.getEmail());
            stmt.setInt(4, profile.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteByUserId(Integer userId) throws SQLException {
        String deleteQuery = "DELETE FROM profiles WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public Profile findById(int profileId) throws SQLException {
        Profile profile = null;
        String query = "SELECT * FROM profiles WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, profileId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                profile = mapRowToProfile(rs);
            }
        }
        return profile;
    }

    private Profile mapRowToProfile(ResultSet rs) throws SQLException {
        Profile profile = new Profile();
        profile.setId(rs.getInt("id"));
        profile.setFirstName(rs.getString("first_name"));
        profile.setLastName(rs.getString("last_name"));
        profile.setEmail(rs.getString("email"));
        int userId = rs.getInt("user_id");
        UserDao userDao = new UserDao(dataSource);
        User user = userDao.findById(userId);
        profile.setUserId(user);

        return profile;
    }


}
