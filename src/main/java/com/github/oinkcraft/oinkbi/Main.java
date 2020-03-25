package com.github.oinkcraft.oinkbi;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.EssentialsTimer;
import com.github.oinkcraft.oinkbi.commands.OptOutCommand;
import com.github.oinkcraft.oinkbi.managers.SQLManager;
import com.github.oinkcraft.oinkbi.objects.ActivityRunnable;
import com.github.oinkcraft.oinkbi.util.MainEventHandler;
import net.ess3.api.IEssentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private FileConfiguration config;
    public static Logger log;
    private static Main instance;
    long activityInterval;

    @Override
    public void onEnable() {
        createConfig();
        saveDefaultConfig();
        log = this.getLogger();
        instance = this;
        this.config = this.getConfig();
        activityInterval = this.config.getLong("activity-interval-mins", 30L);

        SQLManager sql = SQLManager.getInstance();
        boolean hasDB = sql.testConnection();
        if (!hasDB) {
            onDisable();
            getServer().getPluginManager().disablePlugin(this);
        }
        log.log(Level.INFO, "Connected to the database!");
        sql.setUpTables();


        new MainEventHandler(this);

        this.getCommand("oinkbi").setExecutor(new OptOutCommand());

        super.onEnable();
        log.info("Successfully enabled OinkBI " + getDescription().getVersion());
        log.info("Starting runnables");
        Essentials ess = getPlugin(Essentials.class);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new ActivityRunnable(this, ess), 0L, (activityInterval * 20 * 60));
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
