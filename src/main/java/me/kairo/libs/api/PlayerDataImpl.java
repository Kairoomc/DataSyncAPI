package me.kairo.libs.api;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Default implementation of the PlayerData interface.
 */
public final class PlayerDataImpl implements PlayerData {

    private final UUID uuid;
    private final Map<String, Object> data;

    /**
     * Constructs a new PlayerDataImpl for the given player UUID.
     *
     * @param uuid the player's UUID
     */
    public PlayerDataImpl(final UUID uuid) {
        this.uuid = uuid;
        this.data = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final String key) {
        return this.data.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public int getInt(final String key) {
        return (int) this.data.getOrDefault(key, 0);
    }

    /**
     * {@inheritDoc}
     */
    public String getString(final String key) {
        return (String) this.data.getOrDefault(key, "");
    }

    /**
     * {@inheritDoc}
     */
    public boolean getBoolean(final String key) {
        return (boolean) this.data.getOrDefault(key, false);
    }

    /**
     * {@inheritDoc}
     */
    public void set(final String key, final Object value) {
        this.data.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getAll() {
        return this.data;
    }

    private static final Gson gson = new Gson();

    /**
     * Serializes the given PlayerData to a JSON string.
     *
     * @param data the PlayerData to serialize
     * @return the JSON representation
     */
    public static String toJson(final PlayerData data) {
        return gson.toJson(data.getAll());
    }

    /**
     * Deserializes a PlayerDataImpl from a JSON string.
     *
     * @param uuid the player's UUID
     * @param json the JSON data
     * @return a new PlayerDataImpl instance
     */
    public static PlayerData fromJson(final UUID uuid, final String json) {
        final PlayerDataImpl impl = new PlayerDataImpl(uuid);
        final Map<String, Object> map = gson.fromJson(json, Map.class);

        impl.data.putAll(map);
        return impl;
    }
}