package com.github.oinkcraft.oinkbi.objects.stattypes.interfaces;

import org.json.JSONArray;

public interface TimeStat {
    JSONArray getTimeSpentInWorld(String worldName);
    JSONArray getTimeSpentOnline();
}
