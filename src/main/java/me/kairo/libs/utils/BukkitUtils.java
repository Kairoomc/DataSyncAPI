package me.kairo.libs.utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class BukkitUtils {

    private static Plugin plugin;

    public static void init(Plugin pl) {
        plugin = pl;
    }

    /**
     * Exécute du code sur le thread principal
     */
    public static void runSync(Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    /**
     * Exécute du code de manière asynchrone
     */
    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    /**
     * Exécute du code avec un délai en ticks
     */
    public static void runLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    /**
     * Exécute du code en boucle avec un intervalle
     */
    public static void runTimer(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }
}