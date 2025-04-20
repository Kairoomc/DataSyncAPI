package me.kairo.libs.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Utility class for running tasks synchronously or asynchronously on the Bukkit server.
 */
public final class BukkitUtils {

    private static Plugin plugin;

    /**
     * Initializes the utility with the given plugin instance.
     *
     * @param pl the plugin instance to use for scheduling tasks
     */
    public static void init(final Plugin pl) {
        plugin = pl;
    }

    /**
     * Executes code on the main server thread.
     *
     * @param runnable the code to execute
     */
    public static void runSync(final Runnable runnable) {
        if (Bukkit.isPrimaryThread()) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    /**
     * Executes code asynchronously.
     *
     * @param runnable the code to execute
     */
    public static void runAsync(final Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    /**
     * Executes code after a specified delay (in ticks).
     *
     * @param runnable the code to execute
     * @param delay    the delay in ticks before execution
     */
    public static void runLater(final Runnable runnable, final long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    /**
     * Executes code repeatedly with a specified delay and interval (in ticks).
     *
     * @param runnable the code to execute
     * @param delay    the initial delay in ticks before first execution
     * @param period   the interval in ticks between executions
     */
    public static void runTimer(final Runnable runnable, final long delay, final long period) {
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }
    
    /**
     * Executes code on the main server thread immediately.
     *
     * @param runnable the code to execute
     */
    public static void run(final Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }
}