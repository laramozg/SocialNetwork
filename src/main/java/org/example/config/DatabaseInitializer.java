package org.example.config;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void initializeDatabase() throws SQLException, IOException {
        runScript("/sql/schema.sql");
        runScript("/sql/data.sql");
    }

    private void runScript(String scriptPath) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     getClass().getResourceAsStream(scriptPath)))) {
            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
            statement.execute(script.toString());
        }
    }
}
