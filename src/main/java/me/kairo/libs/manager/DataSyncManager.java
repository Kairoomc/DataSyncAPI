package me.kairo.libs.manager;

import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.utils.BukkitUtils;


import java.util.UUID;
import java.util.function.Consumer;

public final class DataSyncManager {

    private final SQLStorage sqlStorage;
    private final RedisManager redisManager;
    private final DataCache dataCache;

    public DataSyncManager(SQLStorage sqlStorage, RedisManager redisManager, DataCache dataCache) {
        this.sqlStorage = sqlStorage;
        this.redisManager = redisManager;
        this.dataCache = dataCache;
    }

    public void getData(UUID uuid, Consumer<PlayerData> callback) {
        if (dataCache.has(uuid)) {
            callback.accept(dataCache.get(uuid));
            return;
        }

         BukkitUtils.runAsync(() -> {
            PlayerData data = sqlStorage.load(uuid);
            dataCache.set(uuid, data);

            BukkitUtils.run(() -> callback.accept(data));
        });
    }

    public void saveAndSync(PlayerData data) {
        BukkitUtils.runAsync(() -> {
            sqlStorage.save(data);
            redisManager.broadcast(data);
        });
    }

    public PlayerData createData(UUID uuid) {
        PlayerDataImpl data = new PlayerDataImpl(uuid);
        dataCache.set(uuid, data);
        return data;
    }

    public DataCache getCache() {
        return this.dataCache;
    }
}
