package com.github.oinkcraft.oinkbi.objects;

import com.earth2me.essentials.Essentials;
import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.objects.stattypes.ActivityStat;
import com.google.common.util.concurrent.Runnables;
import org.bukkit.scheduler.BukkitRunnable;

public class ActivityRunnable implements Runnable {

    private Main main;
    private Essentials essentials;

    public ActivityRunnable(Main main, Essentials essentials) {
        this.main = main;
        this.essentials = essentials;
    }

    @Override
    public void run() {
        int players = main.getServer().getOnlinePlayers().size();
        int entities = main.getServer().getWorlds().stream().mapToInt(world -> world.getEntities().size()).sum();
        new ActivityStat(players, entities, essentials).onRemove();
    }
}
