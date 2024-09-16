package dao;

import org.example.dao.ProfileDao;
import org.example.dao.UserDao;
import org.example.model.Profile;
import org.example.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ProfileDaoTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private ProfileDao profileDao;
    private UserDao userDao;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        postgres.start();
        dataSource = new PGSimpleDataSource() {
            {
                setUrl(postgres.getJdbcUrl());
                setUser(postgres.getUsername());
                setPassword(postgres.getPassword());
            }
        };
        profileDao = new ProfileDao(dataSource);
        userDao = new UserDao(dataSource);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255))")) {
            stmt.execute();
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "CREATE TABLE profiles (id SERIAL PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255)," +
                             " email VARCHAR(255), user_id INTEGER REFERENCES users(id) ON DELETE CASCADE) ")) {
            stmt.execute();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE profiles");
            stmt.execute("DROP TABLE users");
        }
        postgres.stop();
    }

    @Test
    void testSaveAndFindProfileById() throws SQLException {
        User user = createUser();
        int userId = userDao.save(user);
        assertTrue(userId > 0);

        Profile profile = creatProfile(user);
        int profileId = profileDao.save(profile);
        assertTrue(profileId > 0);

        Profile retrievedProfile = profileDao.findById(profileId);
        assertNotNull(retrievedProfile);
        assertEquals(profile.getFirstName(), retrievedProfile.getFirstName());
        assertEquals(profile.getLastName(), retrievedProfile.getLastName());
        assertEquals(profile.getEmail(), retrievedProfile.getEmail());
        assertEquals(profile.getUserId().getId(), retrievedProfile.getUserId().getId());
    }

    @Test
    void testFindProfileByUserId() throws SQLException {
        User user = createUser();
        int userId = userDao.save(user);
        assertTrue(userId > 0);

        Profile profile = creatProfile(user);
        profileDao.save(profile);

        Profile retrievedProfile = profileDao.findByUserId(userId);
        assertNotNull(retrievedProfile);
        assertEquals(profile.getFirstName(), retrievedProfile.getFirstName());
        assertEquals(profile.getLastName(), retrievedProfile.getLastName());
        assertEquals(profile.getEmail(), retrievedProfile.getEmail());
        assertEquals(profile.getUserId().getId(), retrievedProfile.getUserId().getId());
    }

    @Test
    void testUpdateProfile() throws SQLException {
        User user = createUser();
        int userId = userDao.save(user);
        assertTrue(userId > 0);

        Profile profile = creatProfile(user);
        int profileId = profileDao.save(profile);
        assertTrue(profileId > 0);

        profile.setFirstName("Jane");
        profile.setLastName("Smith");
        profile.setEmail("jane.smith@example.com");
        profileDao.update(profile);

        Profile updatedProfile = profileDao.findByUserId(user.getId());
        assertEquals("Jane", updatedProfile.getFirstName());
        assertEquals("Smith", updatedProfile.getLastName());
        assertEquals("jane.smith@example.com", updatedProfile.getEmail());
    }

    @Test
    void testDeleteProfileUserId() throws SQLException {
        User user = createUser();
        int userId = userDao.save(user);
        assertTrue(userId > 0);

        Profile profile = creatProfile(user);
        profileDao.save(profile);

        profileDao.deleteByUserId(userId);

        Profile deletedProfile = profileDao.findByUserId(userId);
        assertNull(deletedProfile);
    }

    private Profile creatProfile(User user) {
        Profile profile = new Profile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setEmail("john.doe@example.com");
        profile.setUserId(user);
        return profile;
    }

    private User createUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        return user;
    }

}