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

/**
 * Handles Redis Pub/Sub communication for player data synchronization.
 */
public final class RedisManager {

    private static final String CHANNEL = "datasync_channel";

    private final Jedis jedisPublisher;
    private final Jedis jedisSubscriber;

    /**
     * Initializes Redis connections and subscribes to the data sync channel.
     *
     * @param plugin the JavaPlugin instance
     * @param config the configuration manager
     */
    public RedisManager(final JavaPlugin plugin, final ConfigManager config) {
        this.jedisPublisher = new Jedis(config.getRedisHost(), config.getRedisPort());
        this.jedisSubscriber = new Jedis(config.getRedisHost(), config.getRedisPort());

        new Thread(() -> this.jedisSubscriber.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(final String channel, final String message) {
                if (!channel.equals(CHANNEL)) return;

                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        final String[] parts = message.split(":", 2);
                        final UUID uuid = UUID.fromString(parts[0]);
                        final String dataJson = parts[1];

                        final PlayerData data = PlayerDataImpl.fromJson(uuid, dataJson);
                        Bukkit.getPluginManager().callEvent(new PlayerDataSyncEvent(uuid, data));
                    } catch (final Exception exception) {
                        plugin.getLogger().warning(String.format("[DataSyncAPI] Redis parsing error: %s", exception.getMessage()));
                    }
                });
            }
        }, CHANNEL)).start();
    }

    /**
     * Publishes player data to the Redis channel for synchronization.
     *
     * @param data the player data to broadcast
     */
    public void broadcast(final PlayerData data) {
        final String message = String.format("%s:%s", data.getUUID().toString(), PlayerDataImpl.toJson(data));

        this.jedisPublisher.publish(CHANNEL, message);
    }
}
