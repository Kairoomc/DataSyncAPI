package me.kairo.libs.api;

import java.util.Map;
import java.util.UUID;

/**
 * Represents player data that can be synchronized across servers.
 */
public interface PlayerData {

    /**
     * Gets the UUID of the player.
     *
     * @return the player's UUID
     */
    UUID getUuid();

    /**
     * Gets the value associated with the specified key.
     *
     * @param key the key to look up
     * @return the value, or null if not present
     */
    Object get(final String key);

    /**
     * Gets the integer value associated with the specified key.
     *
     * @param key the key to look up
     * @return the integer value, or 0 if not present
     */
    int getInt(final String key);

    /**
     * Gets the string value associated with the specified key.
     *
     * @param key the key to look up
     * @return the string value, or an empty string if not present
     */
    String getString(final String key);

    /**
     * Gets the boolean value associated with the specified key.
     *
     * @param key the key to look up
     * @return the boolean value, or false if not present
     */
    boolean getBoolean(final String key);

    /**
     * Sets a value for the specified key.
     *
     * @param key the key to set
     * @param value the value to associate
     */
    void set(final String key, final Object value);

    /**
     * Gets all key-value pairs stored for this player.
     *
     * @return a map of all data
     */
    Map<String, Object> getAll();
}
