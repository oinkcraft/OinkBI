package com.github.oinkcraft.oinkbi.objects.stattypes.interfaces;

import org.json.JSONArray;

public interface InteractionStat {
    JSONArray getPlayerToObjectStat();
    JSONArray getObjetcToPlayerStat();
}
