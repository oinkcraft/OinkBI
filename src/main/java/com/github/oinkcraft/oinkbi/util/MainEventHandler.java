package com.github.oinkcraft.oinkbi.util;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.events.CommandEvent;
import com.github.oinkcraft.oinkbi.events.LogInEvent;
import com.github.oinkcraft.oinkbi.events.LogoutEvent;
import com.github.oinkcraft.oinkbi.events.WorldChangeEvent;
import org.bukkit.event.Listener;

public class MainEventHandler {

    Main main;
    public MainEventHandler(Main main) {
        this.main = main;
        register(new LogInEvent());
        register(new LogoutEvent());
        register(new CommandEvent());
        register(new WorldChangeEvent());
        // main.getServer().getPluginManager().registerEvents(new MobSlayEvent(), main);
    }

    private void register(Listener event) {
        main.getServer().getPluginManager().registerEvents(event, this.main);
    }
}
