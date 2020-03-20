package com.github.oinkcraft.oinkbi.events;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.managers.StatManager;
import com.github.oinkcraft.oinkbi.objects.stattypes.OnlineStat;
import com.github.oinkcraft.oinkbi.objects.stattypes.WorldTimeStat;
import com.github.oinkcraft.oinkbi.util.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LogInEvent implements Listener {

    SQLManager sql = SQLManager.getInstance();
    StatManager statManager = StatManager.getInstance();

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sql.notifyAboutOinkBI(player);
        if (!sql.getTelemetryOption(player.getUniqueId(), "telemetry_shown")) {
            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                player.sendMessage(Constants.TELEMETRY_MESSAGE);
                sql.initialisePlayer(player.getUniqueId());
            }, 40L);
        }
        if (sql.getTelemetryOption(player.getUniqueId(), "is_in")) {
            statManager.addStat(new OnlineStat(player.getUniqueId(), Constants.TABLE_ONLINE_TIME));
            statManager.addStat(new WorldTimeStat(player.getUniqueId(), Constants.TABLE_TIME_IN_WORLD, player.getWorld()));
        }
    }
}
