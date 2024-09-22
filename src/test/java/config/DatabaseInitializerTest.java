package config;

import org.example.config.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

class DatabaseInitializerTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;
    private DatabaseInitializer dbInitializer;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        dbInitializer = new DatabaseInitializer(dataSource);
    }

    @Test
    void testInitializeDatabase() throws SQLException, IOException {
        InputStream schemaStream = this.getClass().getResourceAsStream("/sql/schema.sql");
        InputStream dataStream = this.getClass().getResourceAsStream("/sql/data.sql");

        assert schemaStream != null;
        assert dataStream != null;

        dbInitializer.initializeDatabase();

        verify(statement, times(2)).execute(anyString());
    }
}
