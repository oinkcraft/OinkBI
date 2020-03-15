package com.github.oinkcraft.oinkbi.events;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.managers.StatManager;
import com.github.oinkcraft.oinkbi.objects.stattypes.OnlineStat;
import com.github.oinkcraft.oinkbi.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LogInEvent implements Listener {

    SQLManager sql = SQLManager.getInstance();
    StatManager statManager = StatManager.getInstance();

    public void onPlayerLogin(PlayerLoginEvent event ) {
        Player player = event.getPlayer();

        if (!sql.hasSeenTelemetryMessage(player.getUniqueId())) {
            player.sendMessage(Constants.TELEMETRY_MESSAGE);
            sql.initialisePlayer(player.getUniqueId());
            statManager.addStat(new OnlineStat(player.getUniqueId(), Constants.TABLE_ONLINE_TIME, System.currentTimeMillis() / 1000));
        }
    }
}
