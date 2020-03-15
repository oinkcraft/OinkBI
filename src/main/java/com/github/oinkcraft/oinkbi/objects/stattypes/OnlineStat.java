package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.objects.stattypes.interfaces.TimeStat;
import org.json.JSONArray;

import java.util.UUID;

public class OnlineStat extends StatType implements TimeStat {

    private long loginTime;

    public OnlineStat(UUID uuid, String table, long loginTime) {
        this.uuid = uuid;
        this.loginTime = loginTime;
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

    }
}
