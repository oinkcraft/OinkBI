package com.github.oinkcraft.oinkbi.events;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.managers.StatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class LogoutEvent implements Listener {

    SQLManager sql = SQLManager.getInstance();
    StatManager statManager = StatManager.getInstance();

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        boolean isPlayerOptIn = sql.getTelemetryOption(uuid, "is_in");
        statManager.removeStats(uuid, isPlayerOptIn);
    }
}
