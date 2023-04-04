package com.github.gameoholic.twitchrewards.Commands;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StartRedeemsCommand implements CommandExecutor {
    private TwitchRewards plugin;
    public StartRedeemsCommand(TwitchRewards plugin) {
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
            sender.sendMessage(ChatColor.RED + "Redeem player not set. Use /setredeemplayer <player username/s>");
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

        if (invalidRequest) {
            sender.sendMessage(ChatColor.YELLOW + "It looks like this is your first time using the plugin. \n" +
                    "Please follow the following instructions to set up the plugin:\n" +
                    "1. Go to https://twitchtokengenerator.com/\n2. Select 'Custom Scope Token'.\n3. Scroll down and " +
                    "enable channel:read:redemptions.\n4. Click 'Generate Token' and select 'Authorize'. \n5. " +
                    "Copy the ACCESS TOKEN and CLIENT ID, and set them using /setaccesstoken <access token> and " +
                    "/setclientid <client ID>.\n6. Provide the streamer username using /setstreamer <streamer username>, " +
                    "and select the player/s whom you want redeems to affect using " +
                    "/setredeemplayer <player username/s (separated with spaces)>. \n7. Run /startredeems\n\n" +
                    "Don't forget to create redeems on your Twitch channel and edit the config file to match them.");
            return true;
        }



        plugin.getTwitchManager().startClient(sender);
        return true;
    }
}
