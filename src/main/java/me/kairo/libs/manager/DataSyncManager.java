package me.kairo.libs.manager;

import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.api.SyncCallback;
import me.kairo.libs.config.ConfigManager;
import me.kairo.libs.utils.BukkitUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Manages the synchronization of player data between SQL and Redis.
 */
public final class DataSyncManager {

    private final SQLStorage sql;
    private final RedisManager redis;

    /**
     * Constructs a new DataSyncManager.
     *
     * @param plugin the JavaPlugin instance
     * @param config the configuration manager
     */
    public DataSyncManager(final JavaPlugin plugin, final ConfigManager config) {
        this.sql = new SQLStorage(config);
        this.redis = new RedisManager(plugin, config);
    }

    /**
     * Asynchronously loads player data from SQL and returns it via callback on the main thread.
     *
     * @param uuid the player's UUID
     * @param callback the callback to receive the data
     */
    public void getData(final UUID uuid, final SyncCallback callback) {
        BukkitUtils.runAsync(() -> {
            final PlayerData data = this.sql.load(uuid);

            BukkitUtils.run(() -> callback.onDataReceived(data));
        });
    }

    /**
     * Creates a new PlayerData instance for the given UUID.
     *
     * @param uuid the player's UUID
     * @return a new PlayerData instance
     */
    public PlayerData createData(final UUID uuid) {
        return new PlayerDataImpl(uuid);
    }

    /**
     * Saves player data to SQL and broadcasts it to other servers via Redis asynchronously.
     *
     * @param data the player data to save and sync
     */
    public void saveAndSync(final PlayerData data) {
        BukkitUtils.runAsync(() -> {
            this.sql.save(data);
            this.redis.broadcast(data);
        });
    }
}
