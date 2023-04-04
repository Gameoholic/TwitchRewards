package com.github.gameoholic.twitchrewardsplugin.Commands;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetAccessTokenCommand implements CommandExecutor {
    private TwitchRewardsPlugin plugin;
    public SetAccessTokenCommand(TwitchRewardsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.setaccesstoken")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        if (args[0] == null || args[0].equals("")) {
            sender.sendMessage(ChatColor.RED + "Invalid access token. ");
            return true;
        }
        plugin.getConfig().set("AccessToken", args[0]);
        plugin.saveConfig();
        sender.sendMessage(ChatColor.AQUA + "Set Access Token to " + ChatColor.YELLOW + args[0]);
        return true;
    }
}
