package com.github.gameoholic.twitchrewards.Commands;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetRedeemPlayerCommand implements CommandExecutor {
    private TwitchRewards plugin;
    public SetRedeemPlayerCommand(TwitchRewards plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.setredeemplayer")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        String playersString = "";
        List<String> playerUsernames = new ArrayList<>();
        for (String username: args) {
            playersString += username + " ";
            playerUsernames.add(username);
        }
        if (playersString.equals("")) {
            sender.sendMessage(ChatColor.RED + "Invalid player username/s. Separate usernames using spaces.");
            return true;
        }
        plugin.getConfig().set("RedeemPlayers", playerUsernames);
        plugin.saveConfig();
        sender.sendMessage(ChatColor.AQUA + "Set redeems to affect " + ChatColor.YELLOW + playersString);
        return true;
    }
}
