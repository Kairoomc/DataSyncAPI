package me.kairo.libs.api;

import java.util.Map;
import java.util.UUID;

public interface PlayerData {

    UUID getUUID();

    Object get(String key);
    int getInt(String key);
    String getString(String key);
    boolean getBoolean(String key);

    void set(String key, Object value);
    Map<String, Object> getAll();
}
