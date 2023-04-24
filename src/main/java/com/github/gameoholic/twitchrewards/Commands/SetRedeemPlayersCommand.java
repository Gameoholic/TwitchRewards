package com.github.gameoholic.twitchrewards.Commands;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SetRedeemPlayersCommand implements CommandExecutor {
    private TwitchRewards plugin;
    public SetRedeemPlayersCommand(TwitchRewards plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.setredeemplayers")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        if (!plugin.getAddStreamerCommandCache().containsKey(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "Run /addstreamer <streamer username> first.");
            return true;
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

        String streamerUsername = plugin.getAddStreamerCommandCache().get(sender.getName());

        List<HashMap<String, List<String>>> streamerConfigList = (List<HashMap<String, List<String>>>)
            plugin.getConfig().getList("Streamers");
        if (plugin.getConfig().getList("Streamers") == null)
            streamerConfigList = new ArrayList<>();
        HashMap<String, List<String>> streamerConfigMap = new HashMap<>();

        streamerConfigMap.put(streamerUsername, playerUsernames);

        Optional<HashMap<String, List<String>>> streamerOptional = streamerConfigList.stream()
            .filter(streamerMap -> streamerMap.containsKey(streamerUsername))
            .findFirst();

        if (!streamerOptional.isPresent()) {
            streamerConfigList.add(streamerConfigMap);
            sender.sendMessage(ChatColor.GREEN + "Set redeems from streamer " + ChatColor.YELLOW +
                streamerUsername + ChatColor.GREEN + " to affect " +
                ChatColor.YELLOW + playerUsernames);
        }
        else {
            //If streamer already exists in config file, update it instead
            streamerOptional.get().put(streamerUsername, playerUsernames);
            sender.sendMessage(ChatColor.GREEN + "Updated redeems from streamer " + ChatColor.YELLOW +
                streamerUsername + ChatColor.GREEN + " to affect " +
                ChatColor.YELLOW + playerUsernames);
        }


        plugin.getConfig().set("Streamers", streamerConfigList);
        plugin.saveConfig();

        plugin.setStartRedeemsConfirmed(false);
        return true;
    }
}
