package com.github.gameoholic.twitchrewardsplugin.Commands;

import com.github.gameoholic.twitchrewardsplugin.Twitch.TwitchManager;
import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StartRedeemsCommand implements CommandExecutor {
    private TwitchRewardsPlugin plugin;
    public StartRedeemsCommand(TwitchRewardsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.startredeems")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        List<String> redeemPlayers = plugin.getConfig().getStringList("RedeemPlayers");
        String accessToken = plugin.getConfig().getString("AccessToken");
        String clientId = plugin.getConfig().getString("ClientID");
        String streamerUsername = plugin.getConfig().getString("StreamerUsername");

        if (plugin.getTwitchManager().getTwitchClient() != null) {
            sender.sendMessage(ChatColor.RED + "Twitch Client is already running!");
            return true;
        }
        boolean invalidRequest = false;
        if (redeemPlayers.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Redeem player not set. Use /setredeemplayer <player/s>");
            invalidRequest = true;
        }
        if (accessToken == null) {
            sender.sendMessage(ChatColor.RED + "Twitch access token not set. Use /setaccesstoken <token>");
            invalidRequest = true;
        }
        if (clientId == null) {
            sender.sendMessage(ChatColor.RED + "Twitch client ID not set. Use /setclientid <id>");
            invalidRequest = true;
        }
        if (streamerUsername == null) {
            sender.sendMessage(ChatColor.RED + "Streamer not set. Use /setstreamer <username>");
            invalidRequest = true;
        }

        if (invalidRequest)
            return true;

        plugin.getTwitchManager().startClient(sender);
        return true;
    }
}
