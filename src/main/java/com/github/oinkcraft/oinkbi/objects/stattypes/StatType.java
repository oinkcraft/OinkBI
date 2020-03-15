package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import org.json.JSONArray;

import java.util.UUID;

public abstract class StatType {
    protected String table;
    protected UUID uuid;
    public JSONArray currentStats;

    protected JSONArray getStat(String column) {
        return SQLManager.getInstance().getStat(table, column, uuid);
    }

    public UUID getUUID() {return this.uuid;}

    public abstract void onRemove();
}
