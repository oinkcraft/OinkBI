package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import org.json.JSONArray;

import java.util.UUID;

public class MobSlaysStat extends StatType implements InteractionStat {

    public MobSlaysStat(UUID uuid) {
        this.uuid = uuid;
        this.table = "oinkbi_mob_slay";
        this.currentStats = this.getStat(SQLManager.ALL_COLUMNS);
    }

    @Override
    public JSONArray getPlayerToObjectStat() {
        return this.getStat("playerSlayedMob");
    }

    @Override
    public JSONArray getObjetcToPlayerStat() {
        return this.getStat("mobSlayedPlayer");
    }
}
