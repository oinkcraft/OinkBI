package com.github.oinkcraft.oinkbi.events;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.managers.StatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeEvent implements Listener {

    SQLManager sql = SQLManager.getInstance();
    StatManager statManager = StatManager.getInstance();

    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (sql.getTelemetryOption(player.getUniqueId(), "is_in")) {

        }
    }
}
