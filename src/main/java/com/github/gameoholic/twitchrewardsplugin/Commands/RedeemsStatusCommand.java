package com.github.gameoholic.twitchrewardsplugin.Commands;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RedeemsStatusCommand implements CommandExecutor {
    private TwitchRewardsPlugin plugin;
    public RedeemsStatusCommand(TwitchRewardsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.redeemsstatus")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        String status = ChatColor.GREEN + "CONNECTED";
        if (plugin.getTwitchManager().getTwitchClient() == null)
            status = ChatColor.RED + "OFFLINE";

        sender.sendMessage(ChatColor.YELLOW + " Twitch Client status: " + status);
        return true;
    }
}
