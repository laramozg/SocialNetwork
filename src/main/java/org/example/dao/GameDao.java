package org.example.dao;

import org.example.model.Game;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDao {
    private final DataSource dataSource;

    public GameDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Game> findAll() throws SQLException {
        List<Game> games = new ArrayList<>();
        String query = "SELECT * FROM games";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
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

    public Game findById(int id) throws SQLException {
        Game game =null;
        String query = "SELECT * FROM games WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                game = new Game();
                game.setId(rs.getInt("id"));
                game.setTitle(rs.getString("title"));
                game.setGenre(rs.getString("genre"));
            }
        }
        return game;
    }

    public int save(Game game) throws SQLException {
        int gameId = 0;
        String query = "INSERT INTO games (title, genre) VALUES (?, ?) RETURNING id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getGenre());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                gameId = rs.getInt("id");
                game.setId(gameId);
            }
        }
        return gameId;
    }


}
