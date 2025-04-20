package me.kairo.libs.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages configuration values loaded from the plugin's config file.
 */
public final class ConfigManager {

    private final FileConfiguration config;

    /**
     * Loads the configuration from the given plugin instance.
     *
     * @param plugin the JavaPlugin instance
     */
    public ConfigManager(final JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    /**
     * Gets the MySQL host.
     *
     * @return the MySQL host
     */
    public String getMySQLHost() {
        return this.config.getString("mysql.host", "localhost");
    }

    /**
     * Gets the MySQL port.
     *
     * @return the MySQL port
     */
    public int getMySQLPort() {
        return this.config.getInt("mysql.port", 3306);
    }

    /**
     * Gets the MySQL database name.
     *
     * @return the MySQL database name
     */
    public String getMySQLDatabase() {
        return this.config.getString("mysql.database", "datasync");
    }

    /**
     * Gets the MySQL username.
     *
     * @return the MySQL username
     */
    public String getMySQLUsername() {
        return this.config.getString("mysql.username", "root");
    }

    /**
     * Gets the MySQL password.
     *
     * @return the MySQL password
     */
    public String getMySQLPassword() {
        return this.config.getString("mysql.password", "password");
    }

    /**
     * Gets the Redis host.
     *
     * @return the Redis host
     */
    public String getRedisHost() {
        return this.config.getString("redis.host", "localhost");
    }

    /**
     * Gets the Redis port.
     *
     * @return the Redis port
     */
    public int getRedisPort() {
        return this.config.getInt("redis.port", 6379);
    }
}

