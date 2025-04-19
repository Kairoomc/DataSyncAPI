package me.kairo.libs.manager;

import me.kairo.libs.api.DataSyncAPI;
import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.api.SyncCallback;
import me.kairo.libs.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class DataSyncManager {

    private final SQLStorage sql;
    private final RedisManager redis;

    public DataSyncManager(JavaPlugin plugin, ConfigManager config) {
        this.sql = new SQLStorage(plugin, config);
        this.redis = new RedisManager(plugin, config);
    }

    public void getData(UUID uuid, SyncCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(DataSyncAPI.getInstance(), () -> {
            PlayerData data = sql.load(uuid);
            Bukkit.getScheduler().runTask(DataSyncAPI.getInstance(), () -> callback.onDataReceived(data));
        });
    }

    public PlayerData createData(UUID uuid) {
        return new PlayerDataImpl(uuid);
    }

    public void saveAndSync(PlayerData data) {
        Bukkit.getScheduler().runTaskAsynchronously(DataSyncAPI.getInstance(), () -> {
            sql.save(data);
            redis.broadcast(data);
        });
    }
}
