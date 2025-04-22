package me.kairo.libs.manager;

import me.kairo.libs.api.PlayerData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataCache {

    private final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public void set(UUID uuid, PlayerData data) {
        cache.put(uuid, data);
    }

    public PlayerData get(UUID uuid) {
        return cache.get(uuid);
    }

    public boolean has(UUID uuid) {
        return cache.containsKey(uuid);
    }

    public void remove(UUID uuid) {
        cache.remove(uuid);
    }

    public Map<UUID, PlayerData> getAll() {
        return this.cache;
    }
}
