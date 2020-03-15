package com.github.oinkcraft.oinkbi.managers;

import com.github.oinkcraft.oinkbi.objects.stattypes.StatType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatManager {

    private static StatManager instance;
    private List<StatType> stats = new ArrayList<>();

    private StatManager() {}

    public static StatManager getInstance() {
        return instance == null ? instance = new StatManager() : instance;
    }

    public void addStat(StatType stat) {
        stats.add(stat);
    }

    public void removeStats(UUID uuid) {
        for (StatType stat : stats) {
            if (stat.getUUID() == uuid) {
                stat.onRemove();
            }
        }
        stats.removeIf(stat -> stat.getUUID() == uuid);
    }

    public void removeStat(StatType stat) {
        stat.onRemove();
        stats.remove(stat);
    }
}
