package com.github.oinkcraft.oinkbi.managers;

import com.github.oinkcraft.oinkbi.objects.stattypes.Stat;
import com.github.oinkcraft.oinkbi.objects.stattypes.interfaces.TimeStat;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatManager {

    private static StatManager instance;
    private List<Stat> stats;

    private StatManager() {
        this.stats = new ArrayList<>();
    }

    public static StatManager getInstance() {
        return instance == null ? instance = new StatManager() : instance;
    }

    public void addStat(Stat stat) {
        stats.add(stat);
    }

    public void removeStats(UUID uuid, boolean update) {
        if (update) {
            for (Stat stat : stats) {
                if (stat.getUUID() == uuid) {
                    stat.onRemove();
                }
            }
        }
        stats.removeIf(stat -> stat.getUUID() == uuid);
    }

    public void removeStat(Stat stat, boolean update) {
        if (update) {
            stat.onRemove();
        }
        stats.remove(stat);
    }

    public List<Stat> getStats(UUID uuid) {
        return stats.parallelStream().filter(stat -> stat.getUUID() == uuid).collect(Collectors.toList());
    }

}
