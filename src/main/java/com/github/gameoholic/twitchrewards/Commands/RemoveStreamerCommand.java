package com.github.gameoholic.twitchrewards.Commands;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RemoveStreamerCommand implements CommandExecutor {

    private TwitchRewards plugin;
    public RemoveStreamerCommand(TwitchRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.removestreamer")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        if (args.length == 0 || args[0].equals("")) {
            sender.sendMessage(ChatColor.RED + "Invalid Streamer username. ");
            return true;
        }

        List<HashMap<String, List<String>>> streamerConfigList = (List<HashMap<String, List<String>>>)
            plugin.getConfig().getList("Streamers");
        if (plugin.getConfig().getList("Streamers") == null) {
            sender.sendMessage(ChatColor.RED + "You have not added that streamer!");
            return true;
        }
        args[0] = args[0].toLowerCase();

        Optional<HashMap<String, List<String>>> streamerOptional = streamerConfigList.stream()
            .filter(streamerMap -> streamerMap.containsKey(args[0]))
            .findFirst();

        if (streamerOptional.isPresent()) {
            streamerConfigList.remove(streamerOptional.get());
            plugin.getConfig().set("Streamers", streamerConfigList);
            plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Removed Streamer " + args[0]);
        }
        else {
            sender.sendMessage(ChatColor.RED + "You have not added that streamer!");
        }

        plugin.setStartRedeemsConfirmed(false);
        return true;
    }
}



