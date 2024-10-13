//package dao;
//
//import org.example.repository.GameDao;
//import org.example.repository.ProfileDao;
//import org.example.repository.ProfileGameDao;
//import org.example.repository.UserDao;
//import org.example.model.Game;
//import org.example.model.Profile;
//import org.example.model.User;
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
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class ProfileGameTest {
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
//
//    private DataSource dataSource;
//    private ProfileGameDao profileGameDao;
//    private GameDao gameDao;
//    private ProfileDao profileDao;
//    private UserDao userDao;
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
//        profileGameDao = new ProfileGameDao(dataSource);
//        gameDao = new GameDao(dataSource);
//        profileDao = new ProfileDao(dataSource);
//        userDao = new UserDao(dataSource);
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255));")) {
//            stmt.execute();
//        }
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE profiles (id SERIAL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255)," +
//                             " email VARCHAR(255), user_id INTEGER REFERENCES users(id) ON DELETE CASCADE) ")) {
//            stmt.execute();
//        }
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE games (id SERIAL PRIMARY KEY, title VARCHAR(255), genre VARCHAR(255));")) {
//            stmt.execute();
//        }
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE profile_games (profile_id INTEGER REFERENCES profiles(id) ON DELETE CASCADE, " +
//                             "game_id INTEGER REFERENCES games(id) ON DELETE CASCADE, " +
//                             "PRIMARY KEY (profile_id, game_id));")) {
//            stmt.execute();
//        }
//
//    }
//    @AfterEach
//    void tearDown() throws SQLException {
//        try (var conn = dataSource.getConnection();
//             var stmt = conn.createStatement()) {
//            stmt.execute("DROP TABLE profile_games");
//            stmt.execute("DROP TABLE games");
//            stmt.execute("DROP TABLE profiles");
//            stmt.execute("DROP TABLE users");
//        }
//        postgres.stop();
//    }
//
//    @Test
//    void testAddProfileToGame() throws SQLException {
//        User user = createUser();
//        userDao.save(user);
//
//        Profile profile = creatProfile(user);
//        profileDao.save(profile);
//
//        Game game = new Game();
//        game.setTitle("The Witcher 3");
//        game.setGenre("RPG");
//        gameDao.save(game);
//
//        profileGameDao.addProfileToGame(profile.getId(), game.getId());
//
//        List<Game> games = profileGameDao.findGamesByProfileId(profile.getId());
//        assertEquals(1, games.size());
//        assertEquals(game.getTitle(), games.get(0).getTitle());
//    }
//
//    @Test
//    void testFindGamesByProfileId() throws SQLException {
//        User user = createUser();
//        userDao.save(user);
//
//        Profile profile = creatProfile(user);
//        profileDao.save(profile);
//
//        Game game1 = new Game();
//        game1.setTitle("Game 1");
//        game1.setGenre("Action");
//        gameDao.save(game1);
//
//        Game game2 = new Game();
//        game2.setTitle("Game 2");
//        game2.setGenre("Puzzle");
//        gameDao.save(game2);
//
//        profileGameDao.addProfileToGame(profile.getId(), game1.getId());
//        profileGameDao.addProfileToGame(profile.getId(), game2.getId());
//
//        List<Game> games = profileGameDao.findGamesByProfileId(profile.getId());
//        assertEquals(2, games.size());
//        assertTrue(games.stream().anyMatch(g -> g.getTitle().equals("Game 1")));
//        assertTrue(games.stream().anyMatch(g -> g.getTitle().equals("Game 2")));
//    }
//
//
//    private Profile creatProfile(User user){
//        Profile profile = new Profile();
//        profile.setFirstName("John");
//        profile.setLastName("Doe");
//        profile.setEmail("john.doe@example.com");
//        profile.setUserId(user);
//        return profile;
//    }
//
//    private User createUser(){
//        User user = new User();
//        user.setUsername("user");
//        user.setPassword("password");
//        return user;
//    }
//}
