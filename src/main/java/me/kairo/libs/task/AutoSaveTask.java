package me.kairo.libs.task;




import me.kairo.libs.api.PlayerData;
import me.kairo.libs.api.PlayerDataImpl;
import me.kairo.libs.manager.DataCache;
import me.kairo.libs.utils.BukkitUtils;

import java.util.Map;
import java.util.UUID;

public class AutoSaveTask {

   public static void start(me.kairo.libs.manager.DataSyncManager manager, DataCache cache) {
      BukkitUtils.runTimer(() -> {
       for (Map.Entry<UUID, PlayerData> entry : cache.getAll().entrySet()) {
            PlayerData data = entry.getValue();
            if (data instanceof PlayerDataImpl) {
             PlayerDataImpl impl = (PlayerDataImpl) data;
               if (impl.isDirty()) {
                manager.saveAndSync(data);
                impl.clearDirty();
               }
            }
         }
      }, 20L * 30, 20L * 30);
   }

}
