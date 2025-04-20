package me.kairo.libs.api;

/**
 * Callback interface for receiving player data asynchronously.
 */
public interface SyncCallback {
    /**
     * Called when player data has been received.
     *
     * @param data the received PlayerData
     */
    void onDataReceived(final PlayerData data);
}
