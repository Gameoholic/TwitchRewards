package com.github.gameoholic.twitchrewardsplugin.Commands;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class SetRedeemPlayerCommand implements CommandExecutor {
    private TwitchRewardsPlugin plugin;
    public SetRedeemPlayerCommand(TwitchRewardsPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String playersString = "";
        List<String> playerUsernames = new ArrayList<>();
        for (String username: args) {
            playersString += username + " ";
            playerUsernames.add(username);
        }
        if (playersString.equals("")) {
            sender.sendMessage(ChatColor.RED + "Invalid player username/s. ");
            return true;
        }
        plugin.getConfig().set("RedeemPlayers", playerUsernames);
        plugin.saveConfig();
        sender.sendMessage(ChatColor.AQUA + "Set redeems to affect " + ChatColor.YELLOW + playersString);
        return true;
    }
}
