package com.github.gameoholic.twitchrewardsplugin.Commands;


import com.github.gameoholic.twitchrewardsplugin.Rewards.RewardManager;
import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import org.bukkit.Bukkit;
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
        if (plugin.getTwitchManager().getTwitchClient() != null)
            plugin.getRewardManager().activateChannelPointReward(sender.getName(), args[0].toString(), 20, "");
        return true;
    }



}
