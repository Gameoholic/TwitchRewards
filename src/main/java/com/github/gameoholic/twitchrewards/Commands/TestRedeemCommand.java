package com.github.gameoholic.twitchrewards.Commands;


import com.github.gameoholic.twitchrewards.Rewards.RewardsActivators.AirDrop.AirDrop;
import com.github.gameoholic.twitchrewards.Rewards.RewardsActivators.AirDrop.Rarity;
import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TestRedeemCommand implements CommandExecutor {


    private TwitchRewards plugin;
    public TestRedeemCommand(TwitchRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("twitchrewards.testredeems")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }



        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Incorrect command usage! /testredeem <streamer name> <redeem name>");
            return true;
        }
        String redeemName = "";
        for (int i = 1; i < args.length; i++) {
            redeemName += args[i] + " ";
        }
        if (redeemName.length() > 0)
            redeemName = redeemName.substring(0, redeemName.length() - 1);

        //If Twitch client wasn't set up yet, allow temporary usage of command on command sender
        if (plugin.getTwitchManager().getTwitchClient() == null) {
            if (sender instanceof Player player) {
                List<String> redeemPlayers = new ArrayList<>();
                redeemPlayers.add(player.getName());
                plugin.getRewardManager().activateChannelPointReward(args[0], redeemPlayers, sender.getName(), redeemName, 0, "");
            }
        }
        else {
            //TODO: learn how the fuck this works
            //Pair of streamer ID, and the redeemPlayers
            Pair<String, List<String>> redeemData = plugin.getTwitchManager().getStreamers().stream()
                .map(HashMap::entrySet)
                .flatMap(Collection::stream)
                .filter(entry -> entry.getKey().getLeft().toLowerCase().equals(args[0].toLowerCase()))
                .map(entry -> Pair.of(entry.getKey().getLeft(), entry.getValue()))
                .findFirst()
                .orElse(null);

            if (redeemData == null)  {
                sender.sendMessage(ChatColor.RED + "Invalid streamer username!");
                return true;
            }
            plugin.getRewardManager().activateChannelPointReward(redeemData.getLeft(), redeemData.getRight(), sender.getName(), redeemName, 0, "");
        }
        return true;
    }



}
