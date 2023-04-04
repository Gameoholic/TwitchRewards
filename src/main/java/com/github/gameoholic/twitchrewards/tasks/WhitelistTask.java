package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WhitelistTask extends BukkitRunnable {

    public static List<WhitelistTask> whitelistTasks = new ArrayList<>();

    private int timeLeft;
    private String playerUsername;
    private Player affectedPlayer; //Player who's affected by redeems
    private boolean joined = false;
    public WhitelistTask(TwitchRewards plugin, String playerUsername, Player affectedPlayer, int time) {
        timeLeft = time + 1;
        this.playerUsername = playerUsername;
        this.affectedPlayer = affectedPlayer;
        runTaskTimer(plugin, 0L, 20L);
        whitelistTasks.add(this);
    }
//player might be null when joining

    @Override
    public void run() {
        timeLeft--;
        Player player = Bukkit.getPlayer(playerUsername);
        if (player != null) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.YELLOW + "" + String.format("Whitelist active for " + timeLeft + " seconds")));
        }
        if (timeLeft <= 0) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String command = "whitelist remove " + playerUsername;
            Bukkit.dispatchCommand(console, command);
            if (player != null)
                player.kickPlayer("Whitelist redeem expired!");
            cancel();
            whitelistTasks.remove(this);
        }

    }




    public String getPlayerUsername() {
        return playerUsername;
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
    public boolean hasJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }
    public Player getAffectedPlayer() {
        return affectedPlayer;
    }
}
