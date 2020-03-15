package com.github.oinkcraft.oinkbi.objects.stattypes;

import org.json.JSONArray;

public interface InteractionStat {
    JSONArray getPlayerToObjectStat();
    JSONArray getObjetcToPlayerStat();
}
