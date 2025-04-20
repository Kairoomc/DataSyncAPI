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
    public PlayerData load(final UUID uuid) {
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement stmt = connection.prepareStatement("SELECT data FROM player_data WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            final ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                final String json = rs.getString("data");

                return PlayerDataImpl.fromJson(uuid, json);
            }
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
        return new PlayerDataImpl(uuid);
    }

    /**
     * Saves player data to the database.
     *
     * @param data the PlayerData to save
     */
    public void save(final PlayerData data) {
        final String json = PlayerDataImpl.toJson(data);
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement stmt = connection.prepareStatement(
                     "REPLACE INTO player_data (uuid, data) VALUES (?, ?)")) {
            stmt.setString(1, data.getUUID().toString());
            stmt.setString(2, json);
            stmt.executeUpdate();
        } catch (final SQLException exception) {
            exception.printStackTrace();
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
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_data (" +
                             "uuid VARCHAR(36) PRIMARY KEY, " +
                             "data LONGTEXT NOT NULL)")) {
            stmt.executeUpdate();
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }
}