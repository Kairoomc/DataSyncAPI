package me.kairo.libs.events;

import me.kairo.libs.api.PlayerData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Event triggered when a player's data is synchronized via Redis.
 */
public final class PlayerDataSyncEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final UUID uuid;
    private final PlayerData data;

    /**
     * Constructs a new PlayerDataSyncEvent.
     *
     * @param uuid the UUID of the player whose data was synchronized
     * @param data the synchronized player data
     */
    public PlayerDataSyncEvent(final UUID uuid, final PlayerData data) {
        this.uuid = uuid;
        this.data = data;
    }

    /**
     * Gets the UUID of the player whose data was synchronized.
     *
     * @return the player's UUID
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Gets the synchronized player data.
     *
     * @return the player data
     */
    public PlayerData getData() {
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * {@inheritDoc}
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}