package dao;

import org.example.dao.UserDao;
import org.example.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

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
        userDao = new UserDao(dataSource);

        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, username VARCHAR(255), password VARCHAR(255))");

        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE users");
        }
        postgres.stop();
    }

    @Test
    void testSaveAndFindUserById() throws SQLException {
        User user = createUser();
        int userId = userDao.save(user);
        assertTrue(userId > 0);
        User savedUser = userDao.findById(userId);
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
    }


    @Test
    void testUpdateUser() throws SQLException {
        User user = createUser();
        int userId = userDao.save(user);
        user.setId(userId);
        user.setUsername("updateduser");
        user.setPassword("newpassword123");
        user.setId(userId);
        userDao.update(user);

        User updatedUser = userDao.findById(userId);
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("newpassword123", updatedUser.getPassword());
    }

    @Test
    void testDeleteUserById() throws SQLException {
        int userId = userDao.save(createUser());
        userDao.deleteById(userId);

        User deletedUser = userDao.findById(userId);
        assertNull(deletedUser);
    }


    private User createUser(){
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        return user;
    }

}
