package me.kairo.libs.api;

import me.kairo.libs.config.ConfigManager;
import me.kairo.libs.manager.DataSyncManager;
import me.kairo.libs.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DataSyncAPI extends JavaPlugin {

    private static DataSyncAPI instance;
    private DataSyncManager manager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);

        BukkitUtils.init(this);

        this.manager = new DataSyncManager(this, configManager);

        getLogger().info("[DataSyncAPI] Plugin activé avec succès.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[DataSyncAPI] Plugin désactivé.");
    }

    public static DataSyncAPI getInstance() {
        return instance;
    }

    public DataSyncManager getManager() {
        return manager;
    }
}
