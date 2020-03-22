package com.github.oinkcraft.oinkbi.objects.stattypes;

import com.github.oinkcraft.oinkbi.Main;
import com.github.oinkcraft.oinkbi.managers.SQLManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.github.oinkcraft.oinkbi.util.Constants.*;

@SuppressWarnings("SqlResolve")
public class CommandStat extends Stat {

    private String command;
    SQLManager sql = SQLManager.getInstance();

    public CommandStat(String command) {
        this.command = command;
    }

    @Override
    public void onRemove() {
        sql.executeRaw("INSERT INTO " + TABLE_COMMAND_USAGE + " (command, times) VALUE (?, 1) ON DUPLICATE KEY UPDATE times = times + 1;", this.command);
    }
}
