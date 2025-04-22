package me.kairo.libs.manager;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.Consumer;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.XReadArgs.StreamOffset;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType;
import io.lettuce.core.XReadArgs.StreamOffset.StreamOffsetType.*;

import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.config.ConfigManager;
import me.kairo.libs.events.PlayerDataSyncEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RedisStreamManager {
    private static final String STREAM_KEY = "datasync_stream";
    private static final String GROUP_NAME = "datasync_group";
    private static final String CONSUMER_NAME = "datasync_consumer";

    private final RedisClient redisClient;
    private final StatefulRedisConnection<String, String> connection;
    private final RedisCommands<String, String> commands;
    private final JavaPlugin plugin;

    public RedisStreamManager(JavaPlugin plugin, ConfigManager config) {
        this.plugin = plugin;
        this.redisClient = RedisClient.create("redis://" + config.getRedisHost() + ":" + config.getRedisPort());
        this.connection = redisClient.connect();
        this.commands = connection.sync();

        try {
            commands.xgroupCreate(StreamOffset.from(STREAM_KEY, "0-0"), GROUP_NAME);
        } catch (Exception e) {
            // Group already exists
        }

        startConsumer();
    }
}



