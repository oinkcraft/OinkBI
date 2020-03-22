package com.github.oinkcraft.oinkbi.events;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.objects.stattypes.CommandStat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandEvent implements Listener {

    @EventHandler
    public void onCommandEvent(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        new CommandStat(command).onRemove();
    }
}
