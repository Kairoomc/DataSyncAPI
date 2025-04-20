package me.kairo.libs.api;

import me.kairo.libs.config.ConfigManager;
import me.kairo.libs.manager.DataSyncManager;
import me.kairo.libs.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataSyncAPI extends JavaPlugin {

    private static DataSyncAPI instance;

    private ConfigManager configManager;
    private DataSyncManager dataSyncManager;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.configManager = new ConfigManager(this);

        BukkitUtils.init(this);

        this.dataSyncManager = new DataSyncManager(this, this.configManager);
        this.getLogger().info("[DataSyncAPI] Plugin activé avec succès.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[DataSyncAPI] Plugin désactivé.");
    }

    public static DataSyncAPI getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public DataSyncManager getDataSyncManager() {
        return this.dataSyncManager;
    }
}
