package com.github.gameoholic.twitchrewardsplugin.Commands;


import com.github.gameoholic.twitchrewardsplugin.Rewards.RewardManager;
import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestRedeemCommand implements CommandExecutor {


    private TwitchRewardsPlugin plugin;
    public TestRedeemCommand(TwitchRewardsPlugin plugin) {
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

        String redeemName = "";
        for (String arg: args) {
            redeemName += arg;
        }
        if (plugin.getTwitchManager().getTwitchClient() != null)
            plugin.getRewardManager().activateChannelPointReward(sender.getName(), redeemName, 0, "");
        return true;
    }



}
