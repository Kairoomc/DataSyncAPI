package me.kairo.libs.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.config.ConfigManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Handles SQL storage and retrieval of player data using HikariCP.
 */
public final class SQLStorage {

    private static final int MAXIMUM_POOL_SIZE = 10;

    private final HikariDataSource dataSource;

    /**
     * Initializes the SQL storage with the given configuration.
     *
     * @param config the configuration manager
     */
    public SQLStorage(final ConfigManager config) {
        final HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", config.getMySQLHost(), config.getMySQLPort(), config.getMySQLDatabase()));
        hikari.setUsername(config.getMySQLUsername());
        hikari.setPassword(config.getMySQLPassword());
        hikari.setMaximumPoolSize(MAXIMUM_POOL_SIZE);

        this.dataSource = new HikariDataSource(hikari);
        this.createTable();
    }

    /**
     * Loads player data from the database for the given UUID.
     *
     * @param uuid the player's UUID
     * @return the loaded PlayerData, or a new instance if not found
     */
    public PlayerData load(UUID uuid) {
        PlayerDataImpl data = new PlayerDataImpl(uuid);
        String sql = "SELECT * FROM player_data WHERE uuid = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                data.set("kills", rs.getInt("kills"));
                data.set("deaths", rs.getInt("deaths"));
                data.set("coins", rs.getInt("coins"));
                data.set("isPremium", rs.getBoolean("isPremium"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }


    /**
     * Saves player data to the database.
     *
     * @param data the PlayerData to save
     */
    public void save(PlayerData data) {
        String sql = "REPLACE INTO player_data (uuid, kills, deaths, coins, isPremium) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, data.getUuid().toString());
            stmt.setInt(2, data.getInt("kills"));
            stmt.setInt(3, data.getInt("deaths"));
            stmt.setInt(4, data.getInt("coins"));
            stmt.setBoolean(5, data.getBoolean("isPremium"));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Closes the SQL data source and releases resources.
     */
    public void close() {
        this.dataSource.close();
    }

    /**
     * Creates the player_data table if it does not exist.
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_data (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "kills INT DEFAULT 0, " +
                "deaths INT DEFAULT 0, " +
                "coins INT DEFAULT 0, " +
                "isPremium BOOLEAN DEFAULT FALSE)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}