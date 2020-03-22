package com.github.oinkcraft.oinkbi;

import com.github.oinkcraft.oinkbi.commands.OptOutCommand;
import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.util.MainEventHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private FileConfiguration config;
    public static Logger log;
    private static Main instance;

    @Override
    public void onEnable() {
        log = this.getLogger();
        instance = this;
        this.config = this.getConfig();

        SQLManager sql = SQLManager.getInstance();
        boolean hasDB = sql.testConnection();
        if (!hasDB) {
            onDisable();
            getServer().getPluginManager().disablePlugin(this);
        }
        log.log(Level.INFO, "Connected to the database!");
        sql.setUpTables();

        createConfig();
        saveDefaultConfig();

        new MainEventHandler(this);

        this.getCommand("oinkbi").setExecutor(new OptOutCommand());

        super.onEnable();
        log.info("Successfully enabled OinkBI " + getDescription().getVersion());
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
            getLogger().log(Level.INFO, "No configuration found for OinkBI " + getDescription().getVersion());
            saveDefaultConfig();
        } else {
            getLogger().log(Level.INFO, "Configuration found for OinkBI v" + getDescription().getVersion() + "!");
        }
    }
}
