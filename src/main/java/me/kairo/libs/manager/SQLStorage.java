package me.kairo.libs.manager;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public class SQLStorage {

    private final HikariDataSource dataSource;
    private final Gson gson = new Gson();

    public SQLStorage(JavaPlugin plugin, ConfigManager config) {
        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl("jdbc:mysql://" + config.getMySQLHost() + ":" + config.getMySQLPort() + "/" + config.getMySQLDatabase());
        hikari.setUsername(config.getMySQLUsername());
        hikari.setPassword(config.getMySQLPassword());
        hikari.setMaximumPoolSize(10);

        this.dataSource = new HikariDataSource(hikari);
        createTable();
    }


    private void createTable() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_data (" +
                             "uuid VARCHAR(36) PRIMARY KEY, " +
                             "data LONGTEXT NOT NULL)")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerData load(UUID uuid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT data FROM player_data WHERE uuid = ?")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String json = rs.getString("data");
                return PlayerDataImpl.fromJson(uuid, json);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PlayerDataImpl(uuid); // retourne un objet vide
    }

    public void save(PlayerData data) {
        String json = PlayerDataImpl.toJson(data);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "REPLACE INTO player_data (uuid, data) VALUES (?, ?)")) {
            stmt.setString(1, data.getUUID().toString());
            stmt.setString(2, json);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        dataSource.close();
    }
}