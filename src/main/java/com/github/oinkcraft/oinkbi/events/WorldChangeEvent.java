package com.github.oinkcraft.oinkbi.events;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.managers.StatManager;
import com.github.oinkcraft.oinkbi.objects.stattypes.Stat;
import com.github.oinkcraft.oinkbi.objects.stattypes.WorldTimeStat;
import com.github.oinkcraft.oinkbi.objects.stattypes.interfaces.TimeStat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.stream.Collectors;

import static com.github.oinkcraft.oinkbi.util.Constants.*;

public class WorldChangeEvent implements Listener {

    SQLManager sql = SQLManager.getInstance();
    StatManager statManager = StatManager.getInstance();

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Stat statToRemove = statManager.getStats(player.getUniqueId()).stream()
                .filter(stat -> stat.getUUID() == player.getUniqueId() && stat instanceof WorldTimeStat)
                .filter(stat -> ((WorldTimeStat)stat).world == event.getFrom())
                .findAny().orElse(null);
        if (sql.getTelemetryOption(player.getUniqueId(), "is_in")) {
            if (statToRemove != null)
                statManager.removeStat(statToRemove, sql.getTelemetryOption(player.getUniqueId(), "is_in"));
            statManager.addStat(new WorldTimeStat(player.getUniqueId(), TABLE_TIME_IN_WORLD, player.getWorld()));
        }
    }
}
