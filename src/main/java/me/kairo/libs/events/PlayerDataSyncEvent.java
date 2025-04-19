package me.kairo.libs.events;

import me.kairo.libs.api.PlayerData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerDataSyncEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID playerUUID;
    private final PlayerData data;

    public PlayerDataSyncEvent(UUID playerUUID, PlayerData data) {
        this.playerUUID = playerUUID;
        this.data = data;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public PlayerData getData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}