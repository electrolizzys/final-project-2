package database;

import utils.ConfigReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Rebuilds the local H2 database from testdata/schema.sql + data.sql once per
 * run, so data.sql is the single source of truth: adding an INSERT line there
 * adds a test variation on the next run, and rows are never duplicated.
 */
public final class DatabaseInitializer {

    private static volatile boolean initialized = false;

    private DatabaseInitializer() {
    }

    public static synchronized void initializeIfNeeded() {
        if (initialized) {
            return;
        }
        try (Connection connection = DriverManager.getConnection(
                ConfigReader.get("db.url"), ConfigReader.get("db.username"), ConfigReader.get("db.password"))) {
            runScript(connection, "testdata/schema.sql");
            runScript(connection, "testdata/data.sql");
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("Failed to initialize the local database", e);
        }
        initialized = true;
    }

    private static void runScript(Connection connection, String resourcePath) throws IOException, SQLException {
        String script = readResource(resourcePath);
        try (Statement statement = connection.createStatement()) {
            for (String sql : script.split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                }
            }
        }
    }

    private static String readResource(String path) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (InputStream input = DatabaseInitializer.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IOException("Resource not found on classpath: " + path);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }
            }
        }
        return builder.toString();
    }
}
