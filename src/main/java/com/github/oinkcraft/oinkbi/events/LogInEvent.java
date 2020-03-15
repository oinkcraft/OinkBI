package com.github.oinkcraft.oinkbi.events;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LogInEvent implements Listener {

    SQLManager sql = SQLManager.getInstance();

    public void onPlayerLogin(PlayerLoginEvent event ) {
        Player player = event.getPlayer();

        if (!sql.hasSeenTelemetryMessage(player.getUniqueId())) {
            player.sendMessage(Constants.TELEMETRY_MESSAGE);
        }
    }
}
