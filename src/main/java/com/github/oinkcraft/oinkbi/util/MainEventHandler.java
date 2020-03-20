package com.github.oinkcraft.oinkbi.util;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.events.LogInEvent;
import com.github.oinkcraft.oinkbi.events.LogoutEvent;

public class MainEventHandler {

    public MainEventHandler(Main main) {
        main.getServer().getPluginManager().registerEvents(new LogInEvent(), main);
        main.getServer().getPluginManager().registerEvents(new LogoutEvent(), main);
        // main.getServer().getPluginManager().registerEvents(new MobSlayEvent(), main);
    }
}
