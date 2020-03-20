package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.objects.stattypes.interfaces.TimeStat;
import org.bukkit.World;
import org.json.JSONArray;

import java.util.UUID;

public class WorldTimeStat extends Stat implements TimeStat {

    private long time;
    private World world;

    public WorldTimeStat(UUID uuid, String table, World world) {
        System.out.println(world.getName());
        this.uuid = uuid;
        this.table = table;
        this.world = world;
        this.time = System.currentTimeMillis() / 1000;
    }

    @Override
    public void onRemove() {
        String sqlStatement = "UPDATE " + this.table + " SET time_online = time_online + " + ((System.currentTimeMillis() / 1000) - this.time) + " WHERE uuid = '" + this.uuid + "' AND world = '"+this.world.getName()+"';";
        sql.executeRaw(sqlStatement);
    }

    @Override
    public JSONArray getTimeSpentInWorld(String worldName) {
        return null;
    }

    @Override
    public JSONArray getTimeSpentOnline() {
        return null;
    }
}
