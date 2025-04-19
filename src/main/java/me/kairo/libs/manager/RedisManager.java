package me.kairo.libs.manager;

import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.config.ConfigManager;
import me.kairo.libs.events.PlayerDataSyncEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class RedisManager {

    private final JavaPlugin plugin;
    private final Jedis jedisPublisher;
    private final Jedis jedisSubscriber;

    private static final String CHANNEL = "datasync_channel";

    public RedisManager(JavaPlugin plugin, ConfigManager config) {
        this.plugin = plugin;
        this.jedisPublisher = new Jedis(config.getRedisHost(), config.getRedisPort());
        this.jedisSubscriber = new Jedis(config.getRedisHost(), config.getRedisPort());

        // Écoute les synchros entrantes
        new Thread(() -> {
            jedisSubscriber.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    if (!channel.equals(CHANNEL)) return;

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        try {
                            String[] parts = message.split(":", 2);
                            UUID uuid = UUID.fromString(parts[0]);
                            String dataJson = parts[1];

                            PlayerData data = PlayerDataImpl.fromJson(uuid, dataJson);
                            Bukkit.getPluginManager().callEvent(new PlayerDataSyncEvent(uuid, data));
                        } catch (Exception e) {
                            plugin.getLogger().warning("[DataSyncAPI] Erreur de parsing Redis : " + e.getMessage());
                        }
                    });
                }
            }, CHANNEL);
        }).start();
    }

    public void broadcast(PlayerData data) {
        String message = data.getUUID().toString() + ":" + PlayerDataImpl.toJson(data);
        jedisPublisher.publish(CHANNEL, message);
    }
}
