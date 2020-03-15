package com.github.oinkcraft.oinkbi;

import com.github.oinkcraft.oinkbi.events.LogInEvent;

public class MainEventHandler {

    public MainEventHandler(Main main) {
        main.getServer().getPluginManager().registerEvents(new LogInEvent(), main);
        // main.getServer().getPluginManager().registerEvents(new MobSlayEvent(), main);
    }
}
