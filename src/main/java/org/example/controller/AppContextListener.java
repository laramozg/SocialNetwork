package org.example.controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.config.DatabaseConfig;
import org.example.config.DatabaseInitializer;
import org.example.dao.*;
import org.example.service.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;


@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        DataSource dataSource;
        DatabaseInitializer dbInitializer;
        try {
            dataSource = databaseConfig.getDataSource();
            dbInitializer = new DatabaseInitializer(dataSource);
            dbInitializer.initializeDatabase();
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to configure DataSource", e);
        }

        UserDao userDao = new UserDao(dataSource);
        UserService userService = new UserService(userDao);
        sce.getServletContext().setAttribute("userService", userService);

        ProfileDao profileDao = new ProfileDao(dataSource);
        ProfileService profileService = new ProfileService(profileDao);
        sce.getServletContext().setAttribute("profileService", profileService);

        PostDao postDao = new PostDao(dataSource);
        PostService postService = new PostService(postDao);
        sce.getServletContext().setAttribute("postService", postService);

        GameDao gameDao = new GameDao(dataSource);
        GameService gameService = new GameService(gameDao);
        sce.getServletContext().setAttribute("gameService", gameService);

        ProfileGameDao profileGameDao = new ProfileGameDao(dataSource);
        ProfileGameService profileGameService = new ProfileGameService(profileGameDao);
        sce.getServletContext().setAttribute("profileGameService", profileGameService);

    }

}
