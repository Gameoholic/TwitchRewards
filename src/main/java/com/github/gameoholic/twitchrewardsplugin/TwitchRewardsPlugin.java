package com.github.gameoholic.twitchrewardsplugin;

import com.github.gameoholic.twitchrewardsplugin.Commands.*;
import com.github.gameoholic.twitchrewardsplugin.Rewards.RewardManager;
import com.github.gameoholic.twitchrewardsplugin.Twitch.TwitchManager;
import com.github.gameoholic.twitchrewardsplugin.listeners.PlayerInteractListener;
import com.github.gameoholic.twitchrewardsplugin.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class TwitchRewardsPlugin extends JavaPlugin {

    private RewardManager rewardManager;
    private ConfigManager configManager;
    private TwitchManager twitchManager;

    private List<String> playerUsernames = new ArrayList<>();
    @Override
    public void onEnable() {
        saveDefaultConfig();

        //Managers:
        rewardManager = new RewardManager(this);
        configManager = new ConfigManager(this);
        twitchManager = new TwitchManager(this);

        //Event listeners:
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        //Commands:
        getCommand("testredeem").setExecutor(new TestRedeemCommand(this));
        getCommand("setstreamer").setExecutor(new SetStreamerCommand(this));
        getCommand("setredeemplayer").setExecutor(new SetRedeemPlayerCommand(this));
        getCommand("setclientid").setExecutor(new SetClientIdCommand(this));
        getCommand("setaccesstoken").setExecutor(new SetAccessTokenCommand(this));
        getCommand("startredeems").setExecutor(new StartRedeemsCommand(this));
        getCommand("pauseredeems").setExecutor(new PauseRedeemsCommand(this));
        getCommand("redeemsstatus").setExecutor(new RedeemsStatusCommand(this));


        configManager.loadConfig();


        this.getServer().getScheduler().runTask(this, () -> {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.RED + "WARNING! Reloading isn't fully" +
                    " supported yet, you may encounter issues. Please restart the server instead of reloading it.");
        });

    }

    @Override
    public void onDisable() {
        twitchManager.stopClient();
    }
    public RewardManager getRewardManager() {
        return rewardManager;
    }
    public TwitchManager getTwitchManager() {
        return twitchManager;
    }
    public List<String> getPlayerUsernames() {
        return playerUsernames;
    }

    public void setPlayerUsernames(List<String> playerUsernames) {
        this.playerUsernames = playerUsernames;

    }



}
