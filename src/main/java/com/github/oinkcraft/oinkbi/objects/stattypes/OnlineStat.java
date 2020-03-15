package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.objects.stattypes.interfaces.TimeStat;
import org.json.JSONArray;

public class OnlineStat extends StatType implements TimeStat {
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
}
