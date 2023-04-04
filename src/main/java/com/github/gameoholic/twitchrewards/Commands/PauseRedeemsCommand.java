package com.github.gameoholic.twitchrewards.Commands;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PauseRedeemsCommand implements CommandExecutor {
    private TwitchRewards plugin;
    public PauseRedeemsCommand(TwitchRewards plugin) {
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
