package com.github.oinkcraft.oinkbi.util;

import com.github.oinkcraft.oinkbi.Main;

import static org.bukkit.ChatColor.*;

public final class Constants {
    public final static String TELEMETRY_MESSAGE = GOLD + "!!!!!! NOTICE !!!!!!\n \n" + GREEN + "THIS SERVER IS COLLECTING USAGE STATISTICS. THIS IS TO PROVIDE OUR PLAYERS WITH A BETTER EXPERIENCE WHEN PLAYING ON THE SERVER.\n \n" + RED + "TO OPT OUT OF THIS, USE THE FOLLOWING COMMAND: " + BOLD + "/oinkbi optout\n \n" + RESET + GREEN + "For transparency reasons, the plugin is kept open source, such that you can see what usage statistics are being collected. The code can be found at https://github.com/oinkcraft/oinkbi.";
    public final static String TELEMETRY_NOTIFY_IS_OUT = ""+GOLD + BOLD + "Reminder: " + RESET + GREEN + "You are opted out of the usage statistics. To opt in do: " + AQUA + "/oinkbi optin";
    public final static String TELEMETRY_NOTIFY_IS_IN = ""+GOLD + BOLD + "Thank you" + RESET + GREEN + " for being opted in to the usage statistics.\nYou can opt out at any time by doing: " + AQUA + "/oinkbi optin";

    public final static String TABLE_ONLINE_TIME = "oinkbi_time";
    public final static String TABLE_TELEMETRY = "oinkbi_telemetry";
    public final static String TABLE_TIME_IN_WORLD = "oinkbi_time_in_world";
    public final static String TABLE_COMMAND_USAGE = "oinkbi_command_usage";
    public final static String TABLE_ACTIVITY = "oinkbi_activity";
}
