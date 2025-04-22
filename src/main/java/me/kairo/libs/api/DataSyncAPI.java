package me.kairo.libs.api;

import me.kairo.libs.api.PlayerData;
import me.kairo.libs.config.ConfigManager;
import me.kairo.libs.manager.*;
import me.kairo.libs.task.AutoSaveTask;
import me.kairo.libs.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class DataSyncAPI extends JavaPlugin {

    private static DataSyncAPI instance;

    private ConfigManager configManager;
    private DataSyncManager dataSyncManager;
    private DataCache dataCache;
    private RedisManager redisManager;

    @Override
    public void onEnable() {
        instance = this;

        // Chargement config
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);

        // Init utils
        me.kairo.libs.utils.BukkitUtils.init(this);

        // Init cache et managers
        this.dataCache = new DataCache();
        SQLStorage sqlStorage = new SQLStorage(configManager);
        this.redisManager = new RedisManager(configManager);
        this.dataSyncManager = new DataSyncManager(sqlStorage, redisManager, dataCache);

        // Charger les données des joueurs déjà connectés (reload)
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            dataSyncManager.getData(uuid, data -> dataCache.set(uuid, data));
        }

        // Lancement de la tâche d'auto-save
        AutoSaveTask.start(dataSyncManager, dataCache);

        getLogger().info("[DataSyncAPI] Plugin activé avec succès.");
    }

    @Override
    public void onDisable() {
        if (this.redisManager != null) {
            this.redisManager.close();
        }

        getLogger().info("[DataSyncAPI] Plugin désactivé.");
    }

    public static DataSyncAPI getInstance() {
        return instance;
    }

    public DataSyncManager getDataSyncManager() {
        return this.dataSyncManager;
    }

    public DataCache getDataCache() {
        return this.dataCache;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
}
