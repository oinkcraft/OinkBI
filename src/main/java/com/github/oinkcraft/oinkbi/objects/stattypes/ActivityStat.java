package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.earth2me.essentials.Essentials;
import com.github.oinkcraft.oinkbi.Main;
import org.bukkit.Chunk;

import java.util.Arrays;

import static com.github.oinkcraft.oinkbi.util.Constants.TABLE_ACTIVITY;

public class ActivityStat extends Stat {

    private Chunk chunk;
    private int playerCount;
    private int entityCount;
    private double tps;

    public ActivityStat(int playerCount, int entityCount, Essentials essentials) {
        this.playerCount = playerCount;
        this.entityCount = entityCount;
        this.chunk = getChunk();
        this.tps = essentials.getTimer().getAverageTPS();
    }

    private Chunk getChunk() {
        return Main.getInstance().getServer().getOnlinePlayers()
                .stream()
                .map(p -> p.getWorld().getLoadedChunks())
                .flatMap(Arrays::stream)
                .max((ch1, ch2) -> Integer.compare(ch1.getEntities().length, ch2.getEntities().length)).orElse(null);
    }

    private String chunkToString() {
        return this.chunk != null ? "{chunk=["+this.chunk.getWorld().getName()+"]x="+this.chunk.getX()+",z="+this.chunk.getZ() +",entities="+this.chunk.getEntities().length+"}" : "No data available";
    }

    @Override
    public void onRemove() {
        sql.executeRaw("INSERT INTO " + TABLE_ACTIVITY + " (player_count, entity_count, most_populated_chunk, tps) VALUE (?,?,?,?);", this.playerCount, this.entityCount, this.chunkToString(), this.tps);
    }
}
