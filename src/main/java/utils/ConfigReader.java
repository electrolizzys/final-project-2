package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads execution settings from config.properties, with system properties
 * (e.g. -Dbrowser=firefox -Dheadless=false) taking precedence for CI/local overrides.
 */
public final class ConfigReader {

    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config.properties", e);
        }
        return properties;
    }

    public static String get(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static String baseUrl() {
        return get("baseUrl");
    }

    public static int defaultTimeoutMs() {
        return Integer.parseInt(get("defaultTimeoutMs"));
    }

    public static String apiBaseUrl() {
        return get("api.baseUrl");
    }
}
