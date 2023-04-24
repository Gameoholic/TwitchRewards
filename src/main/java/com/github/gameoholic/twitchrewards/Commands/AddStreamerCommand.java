package com.github.gameoholic.twitchrewards.Commands;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddStreamerCommand implements CommandExecutor {
    private TwitchRewards plugin;
    public AddStreamerCommand(TwitchRewards plugin) {
            this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
                if (!player.hasPermission("twitchrewards.addstreamer")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                        return true;
                }
        }

        if (args.length == 0 || args[0].equals("")) {
                sender.sendMessage(ChatColor.RED + "Invalid Streamer username. ");
                return true;
        }

        args[0] = args[0].toLowerCase();
        plugin.getAddStreamerCommandCache().put(sender.getName(), args[0]);
        sender.sendMessage(ChatColor.AQUA + "Selected Streamer " + ChatColor.YELLOW + args[0] + ChatColor.GREEN + "." +
            ChatColor.AQUA + "\nRun /setredeemplayers <usernames> to set the players whom" +
            " you want redeems to affect.");

        plugin.setStartRedeemsConfirmed(false);

        return true;
    }
}
