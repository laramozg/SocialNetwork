package org.example.dao;

import org.example.model.Game;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfileGameDao {
    private final DataSource dataSource;

    public ProfileGameDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addProfileToGame(int profileId, int gameId) throws SQLException {
        String query = "INSERT INTO profile_games (profile_id, game_id) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, profileId);
            ps.setInt(2, gameId);
            ps.executeUpdate();
        }
    }

    public List<Game> findGamesByProfileId(int profileId) throws SQLException {
        List<Game> games = new ArrayList<>();
        String query = "SELECT g.* FROM games g " +
                "JOIN profile_games pg ON g.id = pg.game_id " +
                "WHERE pg.profile_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, profileId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setGenre(rs.getString("genre"));
                games.add(game);
            }
        }
        return games;
    }
}
