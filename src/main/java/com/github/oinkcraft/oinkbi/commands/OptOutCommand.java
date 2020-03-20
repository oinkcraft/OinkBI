package com.github.oinkcraft.oinkbi.commands;

import com.github.oinkcraft.oinkbi.managers.SQLManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.github.oinkcraft.oinkbi.util.Constants.TABLE_TELEMETRY;
import static org.bukkit.ChatColor.*;

public class OptOutCommand implements CommandExecutor {
    SQLManager sql = SQLManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length != 1) {
            invalidCommand(sender);
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(RED + "You must be a player to perform this command.");
        } else {
            Player player = (Player)sender;
            if (sql.getTelemetryOption(player.getUniqueId(), "is_in")) {
                if (args[0].equalsIgnoreCase("optin")) {
                    sender.sendMessage(GREEN + "We are glad you want to help! You are already in the static logging.");
                } else if (args[0].equalsIgnoreCase("optout")) {
                    sender.sendMessage(GREEN + "Sorry to see you leave the programme. You have been removed from the logging.");
                    sql.executeRaw("UPDATE "+TABLE_TELEMETRY+" SET is_in = 0 WHERE uuid = '" + player.getUniqueId() + "'");
                } else {
                    invalidCommand(sender);
                }
            } else {
                if (args[0].equalsIgnoreCase("optin")) {
                    sender.sendMessage(GREEN + "We are glad you want to help! You have been added to the logging again.");
                    sql.executeRaw("UPDATE "+TABLE_TELEMETRY+" SET is_in = 1 WHERE uuid = '" + player.getUniqueId() + "'");
                } else if (args[0].equalsIgnoreCase("optout")) {
                    sender.sendMessage(GREEN + "You have already opted out of the programme.");
                } else {
                    invalidCommand(sender);
                }
            }
        }
        return true;
    }

    private void invalidCommand(CommandSender sender) {
        sender.sendMessage(RED + "Invalid command. Your possibilities are to opt out or in.");
        sender.sendMessage(RED + "   /oinkbi optout");
        sender.sendMessage(RED + "   /oinkbi optin");
        sender.sendMessage(RED + "   /oinkbi help");
    }
}
