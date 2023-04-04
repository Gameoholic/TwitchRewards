package com.github.gameoholic.twitchrewards.Commands;


import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        String redeemName = "";
        for (String arg: args) {
            redeemName += arg + " ";
        }
        if (redeemName.length() > 0)
            redeemName = redeemName.substring(0, redeemName.length() - 1);
        if (plugin.getTwitchManager().getTwitchClient() != null)
            plugin.getRewardManager().activateChannelPointReward(sender.getName(), redeemName, 0, "");
        return true;
    }



}
