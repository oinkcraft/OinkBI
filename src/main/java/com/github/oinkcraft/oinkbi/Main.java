package com.github.oinkcraft.oinkbi;

import com.github.oinkcraft.oinkbi.objects.BIStat;
import com.github.oinkcraft.oinkbi.objects.stattypes.MobSlaysStat;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private FileConfiguration config;
    private Logger log;
    private static Main instance;

    @Override
    public void onEnable() {
        this.log = this.getLogger();
        instance = this;
        this.config = this.getConfig();

        BIStat stat = new BIStat(new MobSlaysStat(UUID.randomUUID()));

        createConfig();
        saveDefaultConfig();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static Main getInstance() {
        return instance;
    }

    private void createConfig() {
        if (!getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            getDataFolder().mkdirs();
        }
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            getLogger().log(Level.INFO, "No configuration found for DiscordMC2.0 " + getDescription().getVersion());
            saveDefaultConfig();
        } else {
            getLogger().log(Level.INFO, "Configuration found for DiscordMC2.0 v" + getDescription().getVersion() + "!");
        }
    }

    public Logger getLog() {
        return this.log;
    }
}
