//package dao;
//
//import org.example.repository.PostDao;
//import org.example.repository.ProfileDao;
//import org.example.repository.UserDao;
//import org.example.model.Post;
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
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PostDoaTest {
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
//
//    private DataSource dataSource;
//    private PostDao postDao;
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
//        userDao = new UserDao(dataSource);
//        profileDao = new ProfileDao(dataSource);
//        postDao = new PostDao(dataSource);
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255))")) {
//            stmt.execute();
//        }
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE profiles (id SERIAL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255)," +
//                             "email VARCHAR(255), user_id INTEGER REFERENCES users(id) ON DELETE CASCADE) ")) {
//            stmt.execute();
//        }
//
//        try (Connection conn = dataSource.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(
//                     "CREATE TABLE posts (id SERIAL PRIMARY KEY, profile_id INTEGER REFERENCES profiles(id) ON DELETE CASCADE, " +
//                             "content TEXT, created_at TIMESTAMP)")) {
//            stmt.execute();
//        }
//    }
//
//    @AfterEach
//    void tearDown() throws SQLException {
//        try (var conn = dataSource.getConnection();
//             var stmt = conn.createStatement()) {
//            stmt.execute("DROP TABLE posts");
//            stmt.execute("DROP TABLE profiles");
//            stmt.execute("DROP TABLE users");
//        }
//        postgres.stop();
//    }
//
//    @Test
//    void testSaveAndFindPostByProfileId() throws SQLException {
//        User user = createUser();
//        int userId = userDao.save(user);
//        assertTrue(userId > 0);
//
//        Profile profile = creatProfile(user);
//        int profileId = profileDao.save(profile);
//        assertTrue(profileId > 0);
//
//        Post post = createPost(profile);
//        postDao.save(post);
//
//        List<Post> posts = postDao.findByProfileId(profileId);
//        assertNotNull(posts);
//        assertEquals(1, posts.size());
//        assertEquals("This is a test post.", posts.get(0).getContent());
//    }
//
//    @Test
//    void testDeletePost() throws SQLException {
//        User user = createUser();
//        int userId = userDao.save(user);
//        assertTrue(userId > 0);
//
//        Profile profile = creatProfile(user);
//        int profileId = profileDao.save(profile);
//        assertTrue(profileId > 0);
//
//        Post post = createPost(profile);
//        postDao.save(post);
//
//        List<Post> posts = postDao.findByProfileId(profileId);
//        assertNotNull(posts);
//        assertEquals(1, posts.size());
//
//        postDao.delete(posts.get(0).getId());
//
//        List<Post> remainingPosts = postDao.findByProfileId(profileId);
//        assertTrue(remainingPosts.isEmpty());
//    }
//
//    private Post createPost(Profile profile){
//        Post post = new Post();
//        post.setProfileId(profile);
//        post.setContent("This is a test post.");
//        post.setCreatedAt(LocalDateTime.now());
//        return post;
//    }
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
//
//
//
