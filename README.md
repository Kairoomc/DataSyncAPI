# 🧠 DataSyncAPI

**DataSyncAPI** est une librairie Java pour les plugins Minecraft, conçue pour **synchroniser les données des joueurs** entre plusieurs serveurs dans un network (Bungee/Folia-ready).  
Elle utilise **MySQL** pour la persistance et **Redis Pub/Sub** pour la communication en temps réel.

---

## ✨ Fonctionnalités

->  Synchro temps réel via Redis
->  Persistance SQL (MySQL) avec HikariCP
->  Support de Paper, Spigot, Folia
->  Intégration simple pour n’importe quel plugin
->  Event `PlayerDataSyncEvent` déclenché automatiquement

---

## 📦 Installation (Gradle)

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.kairoomc:DataSyncAPI:1.0.0")
}

## 🔧 Exemple d'intégration


// Lecture
DataSyncAPI.getInstance().getManager().getData(player.getUniqueId(), data -> {
    int points = data.getInt("points");
    player.sendMessage("Tu as " + points + " points.");
});

// Écriture
PlayerData data = DataSyncAPI.getInstance().getManager().createData(player.getUniqueId());
data.set("points", 250);
DataSyncAPI.getInstance().getManager().saveAndSync(data);

// Events
@EventHandler
public void onDataSync(PlayerDataSyncEvent event) {
    PlayerData data = event.getData();
    Bukkit.getLogger().info("Données synchronisées pour : " + event.getPlayerUUID());
}
