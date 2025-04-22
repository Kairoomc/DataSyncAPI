package me.kairo.libs.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataImpl implements PlayerData {

    private final UUID uuid;
    private final Map<String, Object> data = new HashMap<>();
    private boolean dirty = false;

    private static final ObjectMapper mapper = new ObjectMapper();

    public PlayerDataImpl(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public int getInt(String key) {
        Object value = data.get(key);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    @Override
    public String getString(String key) {
        Object value = data.get(key);
        return value instanceof String ? (String) value : "";
    }

    @Override
    public boolean getBoolean(String key) {
        Object value = data.get(key);
        return value instanceof Boolean ? (Boolean) value : false;
    }

    @Override
    public void set(String key, Object value) {
        data.put(key, value);
        markDirty();
    }

    @Override
    public Map<String, Object> getAll() {
        return data;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void clearDirty() {
        this.dirty = false;
    }

    public static String toJson(PlayerData data) {
        try {
            return mapper.writeValueAsString(data.getAll());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static PlayerData fromJson(UUID uuid, String json) {
        PlayerDataImpl impl = new PlayerDataImpl(uuid);
        try {
            Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            impl.data.putAll(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return impl;
    }
}
