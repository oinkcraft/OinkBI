package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.objects.stattypes.interfaces.TimeStat;
import org.json.JSONArray;

import java.util.UUID;

public class OnlineStat extends Stat implements TimeStat {

    private long loginTime;

    public OnlineStat(UUID uuid, String table) {
        this.uuid = uuid;
        this.loginTime = System.currentTimeMillis() / 1000;
        this.table = table;
    }

    @Override
    public JSONArray getTimeSpentInWorld(String worldName) {
        // TODO IMPLEMENT
        return null;
    }

    @Override
    public JSONArray getTimeSpentOnline() {
        // TODO IMPLEMENT
        return null;
    }

    @Override
    public void onRemove() {
        String sqlStatement = "UPDATE " + this.table + " SET time_online = time_online + " + ((System.currentTimeMillis() / 1000) - this.loginTime) + " WHERE uuid = '" + this.uuid + "';";
        sql.executeRaw(sqlStatement);
    }
}
