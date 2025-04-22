package me.kairo.libs.manager;

import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.config.ConfigManager;
import me.kairo.libs.events.PlayerDataSyncEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.*;
import redis.clients.jedis.resps.StreamEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages Redis pub/sub communication for player data synchronization.
 */
public final class RedisManager {

    private static final String CHANNEL = "datasync_channel";
    private static final String STREAM = "datasync_stream";

    private final JedisPool jedisPool;

    public RedisManager(final ConfigManager config) {
        this.jedisPool = new JedisPool(config.getRedisHost(), config.getRedisPort());
        listenForChanges();
    }

    public void broadcast(final PlayerData data) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> map = new HashMap<>();
            map.put("uuid", data.getUuid().toString());
            map.put("json", PlayerDataImpl.toJson(data));
            jedis.xadd(STREAM, StreamEntryID.NEW_ENTRY, map);
        }
    }

    private void listenForChanges() {
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String lastId = "$";

                while (true) {
                    List<Map.Entry<String, List<StreamEntry>>> messages = jedis.xread(0, 0, new AbstractMap.SimpleEntry<>(STREAM, new StreamEntryID(lastId)));

                    if (messages != null) {
                        for (Map.Entry<String, List<StreamEntry>> entry : messages) {
                            for (StreamEntry streamEntry : entry.getValue()) {
                                Map<String, String> fields = streamEntry.getFields();
                                String uuid = fields.get("uuid");
                                String json = fields.get("json");

                                Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(RedisManager.class), () -> {
                                    UUID u = UUID.fromString(uuid);
                                    PlayerData data = PlayerDataImpl.fromJson(u, json);
                                    Bukkit.getPluginManager().callEvent(new PlayerDataSyncEvent(u, data));
                                });

                                lastId = streamEntry.getID().toString();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public void close() {
        jedisPool.close();
    }
}