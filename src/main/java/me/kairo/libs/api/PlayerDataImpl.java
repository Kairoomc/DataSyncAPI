package me.kairo.libs.api;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataImpl implements PlayerData {

    private final UUID uuid;
    private final Map<String, Object> data;

    public PlayerDataImpl(UUID uuid) {
        this.uuid = uuid;
        this.data = new HashMap<>();
    }

    public UUID getUUID() { return uuid; }

    public Object get(String key) { return data.get(key); }

    public int getInt(String key) { return (int) data.getOrDefault(key, 0); }

    public String getString(String key) { return (String) data.getOrDefault(key, ""); }

    public boolean getBoolean(String key) { return (boolean) data.getOrDefault(key, false); }

    public void set(String key, Object value) { data.put(key, value); }

    public Map<String, Object> getAll() { return data; }

    private static final Gson gson = new Gson();

    public static String toJson(PlayerData data) {
        return gson.toJson(data.getAll());
    }

    public static PlayerData fromJson(UUID uuid, String json) {
        PlayerDataImpl impl = new PlayerDataImpl(uuid);
        Map<String, Object> map = gson.fromJson(json, Map.class);
        impl.data.putAll(map);
        return impl;
    }
}