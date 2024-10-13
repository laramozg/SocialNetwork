//package dao;
//
//import org.example.repository.GameDao;
//import org.example.model.Game;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.postgresql.ds.PGSimpleDataSource;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameDaoTest {
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
//
//    private DataSource dataSource;
//    private GameDao gameDao;
//
//    @BeforeEach
//    void setUp() throws SQLException {
//        postgres.start();
//        dataSource = new PGSimpleDataSource() {
//            {
//                setUrl(postgres.getJdbcUrl());
//                setUser(postgres.getUsername());
//                setPassword(postgres.getPassword());
//            }
//        };
//
//        gameDao = new GameDao(dataSource);
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE games (id SERIAL PRIMARY KEY, title VARCHAR(255), genre VARCHAR(255))")) {
//            stmt.execute();
//        }
//    }
//    @AfterEach
//    void tearDown() throws SQLException {
//        try (var conn = dataSource.getConnection();
//             var stmt = conn.createStatement()) {
//            stmt.execute("DROP TABLE games");
//        }
//        postgres.stop();
//    }
//
//    @Test
//    void testSaveAndFindAll() throws SQLException {
//        Game game = createGame();
//        gameDao.save(game);
//
//        List<Game> games = gameDao.findAll();
//        assertNotNull(games);
//        assertEquals(1, games.size());
//        assertEquals(game.getTitle(), games.get(0).getTitle());
//        assertEquals(game.getGenre(), games.get(0).getGenre());
//    }
//
//    @Test
//    void testFindById() throws SQLException {
//        Game game = createGame();
//        gameDao.save(game);
//
//        List<Game> games = gameDao.findAll();
//        assertEquals(1, games.size());
//        int gameId = games.get(0).getId();
//
//        Game retrievedGame = gameDao.findById(gameId);
//        assertNotNull(retrievedGame);
//        assertEquals(game.getTitle(), retrievedGame.getTitle());
//        assertEquals(game.getGenre(), retrievedGame.getGenre());
//    }
//
//    @Test
//    void testFindByIdNotFound() throws SQLException {
//        Game game = gameDao.findById(999);
//        assertNull(game);
//    }
//
//    private Game createGame() {
//        Game game = new Game();
//        game.setTitle("The Witcher 3");
//        game.setGenre("RPG");
//        return game;
//    }
//
//}
