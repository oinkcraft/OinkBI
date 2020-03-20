package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.objects.stattypes.interfaces.InteractionStat;
import org.json.JSONArray;

import java.util.UUID;

public class MobSlaysStat extends Stat implements InteractionStat {

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
    public JSONArray getObjectToPlayerStat() {
        return this.getStat("mobSlayedPlayer");
    }

    @Override
    public void onRemove() {

    }
}
