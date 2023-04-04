package com.github.gameoholic.twitchrewardsplugin.Commands;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PauseRedeemsCommand implements CommandExecutor {
    private TwitchRewardsPlugin plugin;
    public PauseRedeemsCommand(TwitchRewardsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.pauseredeems")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "WARNING! This command isn't fully supported yet, and you may encounter " +
                "issues. To disable redeems, please restart the server.");
        sender.sendMessage(ChatColor.YELLOW + "Paused redeems.");
        plugin.getTwitchManager().stopClient();
        return true;
    }
}
