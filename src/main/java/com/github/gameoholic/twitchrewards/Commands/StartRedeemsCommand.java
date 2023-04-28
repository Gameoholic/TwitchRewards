package com.github.gameoholic.twitchrewards.commands;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
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

        String accessToken = plugin.getConfig().getString("AccessToken");
        String clientId = plugin.getConfig().getString("ClientID");
        List<HashMap<String, List<String>>> streamerConfigList = (List<HashMap<String, List<String>>>)
            plugin.getConfig().getList("Streamers");

        if (plugin.getTwitchManager().getTwitchClient() != null) {
            sender.sendMessage(ChatColor.RED + "Twitch Client is already running!");
            return true;
        }
        boolean invalidRequest = false;
        if (accessToken == null) {
            sender.sendMessage(ChatColor.RED + "Twitch access token not set. Use /setaccesstoken <token>");
            invalidRequest = true;
        }
        if (clientId == null) {
            sender.sendMessage(ChatColor.RED + "Twitch client ID not set. Use /setclientid <id>");
            invalidRequest = true;
        }
        if (streamerConfigList == null || streamerConfigList.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Twitch streamers not set. Use /addstreamer <twitch username>");
            invalidRequest = true;
        }

        if (invalidRequest) {
            Boolean usedBefore = plugin.getConfig().getBoolean("UsedBefore");
            if (!usedBefore) {
                TextComponent message = new TextComponent(ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.GREEN + "It seems like it's" +
                        " your first time using the plugin. Please refer to the documentation to set up the plugin: ");
                TextComponent link = new TextComponent("https://github.com/Gameoholic/TwitchRewards/wiki/Setting-Up#linking-to-twitch");
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Gameoholic/TwitchRewards/wiki/Setting-Up#linking-to-twitch"));
                link.setColor(net.md_5.bungee.api.ChatColor.BLUE);
                message.addExtra(link);
                message.addExtra(ChatColor.GREEN + " \nIt will take less than a minute! \nIf you need further assistance," +
                        " contact Gameoholic#8987 on Discord for help.");
                Bukkit.spigot().broadcast(message);
            }
            return true;
        }


        if (!plugin.isStartRedeemsConfirmed()) {
            plugin.setStartRedeemsConfirmed(true);
            sender.sendMessage(ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.GREEN + "Selected streamers:");

            for (HashMap<String, List<String>> streamerByUsername: streamerConfigList) {
                String streamerUsername = (String) streamerByUsername.keySet().toArray()[0];
                List<String> redeemPlayers = streamerByUsername.get(streamerUsername);

                sender.sendMessage(ChatColor.GREEN + streamerUsername + ChatColor.YELLOW + "'s redeems will affect: " +
                    ChatColor.AQUA + redeemPlayers);
            }
            sender.sendMessage(ChatColor.YELLOW + "To modify, use /addstreamer <username> and /removestreamer <username>.\n" +
                ChatColor.GREEN + "To start listening to redeems, run /startredeems again.");
        }
        else {
            plugin.setStreamerList(streamerConfigList);

            plugin.getConfig().set("UsedBefore", true);
            plugin.saveConfig();

            plugin.getTwitchManager().startClient(sender);
            plugin.setStartRedeemsConfirmed(false);
        }
        return true;
    }
}
