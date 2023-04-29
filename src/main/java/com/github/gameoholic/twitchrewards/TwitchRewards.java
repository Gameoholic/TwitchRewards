package com.github.gameoholic.twitchrewards;

import com.github.gameoholic.twitchrewards.commands.*;
import com.github.gameoholic.twitchrewards.rewards.RewardManager;
import com.github.gameoholic.twitchrewards.twitch.TwitchManager;
import com.github.gameoholic.twitchrewards.listeners.PlayerInteractListener;
import com.github.gameoholic.twitchrewards.listeners.PlayerJoinListener;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class TwitchRewards extends JavaPlugin {

    private RewardManager rewardManager;
    private ConfigManager configManager;
    private TwitchManager twitchManager;
    private boolean startRedeemsConfirmed = false;
    private Map<String, String> addStreamerCommandCache = new HashMap<String, String>(); //Executor username -> Streamer username
    private List<HashMap<String, List<String>>> streamerList;
    private Sound sound;
    private String redeemMessage;
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
        getCommand("addstreamer").setExecutor(new AddStreamerCommand(this));
        getCommand("removestreamer").setExecutor(new RemoveStreamerCommand(this));
        getCommand("setredeemplayers").setExecutor(new SetRedeemPlayersCommand(this));
        getCommand("setclientid").setExecutor(new SetClientIdCommand(this));
        getCommand("setaccesstoken").setExecutor(new SetAccessTokenCommand(this));
        getCommand("startredeems").setExecutor(new StartRedeemsCommand(this));
        getCommand("pauseredeems").setExecutor(new PauseRedeemsCommand(this));
        getCommand("redeemsstatus").setExecutor(new RedeemsStatusCommand(this));


        configManager.loadConfig();



        Boolean usedBefore = getConfig().getBoolean("UsedBefore");
        if (!usedBefore)
            getLogger().log(Level.INFO, ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.GREEN + "It seems like it's" +
                " your first time using the plugin. Please refer to the documentation to set up the plugin: " +
                "https://github.com/Gameoholic/TwitchRewards/wiki/Setting-Up or contact Gameoholic#8987 on Discord for help.");
        else
            getLogger().log(Level.INFO, ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.RED + "WARNING! Reloading isn't fully" +
                " supported yet, you may encounter issues. Please restart the server instead.");




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
    public List<HashMap<String, List<String>>> getStreamerList() {
        return streamerList;
    }

    public void setStreamerList(List<HashMap<String, List<String>>> streamerList) {
        this.streamerList = streamerList;
    }
    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }
    public Map<String, String> getAddStreamerCommandCache() {
        return addStreamerCommandCache;
    }
    public boolean isStartRedeemsConfirmed() {
        return startRedeemsConfirmed;
    }

    public void setStartRedeemsConfirmed(boolean startRedeemsConfirmed) {
        this.startRedeemsConfirmed = startRedeemsConfirmed;
    }

    public String getRedeemMessage() {
        return redeemMessage;
    }

    public void setRedeemMessage(String redeemMessage) {
        this.redeemMessage = redeemMessage;
    }



}
