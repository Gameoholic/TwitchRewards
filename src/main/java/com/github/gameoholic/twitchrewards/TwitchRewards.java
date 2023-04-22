package com.github.gameoholic.twitchrewards;

import com.github.gameoholic.twitchrewards.Commands.*;
import com.github.gameoholic.twitchrewards.Rewards.RewardManager;
import com.github.gameoholic.twitchrewards.Twitch.TwitchManager;
import com.github.gameoholic.twitchrewards.listeners.PlayerInteractListener;
import com.github.gameoholic.twitchrewards.listeners.PlayerJoinListener;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class TwitchRewards extends JavaPlugin {

    private RewardManager rewardManager;
    private ConfigManager configManager;
    private TwitchManager twitchManager;
    private List<String> playerUsernames = new ArrayList<>();
    private Sound sound;
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



        Boolean usedBefore = getConfig().getBoolean("UsedBefore");
        if (!usedBefore)
            this.getServer().getScheduler().runTask(this, () -> {
                TextComponent message = new TextComponent(ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.GREEN + "It seems like it's" +
                        " your first time using the plugin. Please refer to the documentation to set up the plugin: ");
                TextComponent link = new TextComponent("https://github.com/Gameoholic/TwitchRewards/wiki/Setting-Up");
                link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Gameoholic/TwitchRewards/wiki/Setting-Up"));
                link.setColor(net.md_5.bungee.api.ChatColor.BLUE);
                message.addExtra(link);
                message.addExtra(ChatColor.GREEN + " or contact Gameoholic#8987 on Discord for help.");
                Bukkit.spigot().broadcast(message);
            });
        else
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
    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }



}
