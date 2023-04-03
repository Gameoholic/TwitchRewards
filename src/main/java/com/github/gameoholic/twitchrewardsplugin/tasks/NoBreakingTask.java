package com.github.gameoholic.twitchrewardsplugin.tasks;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class NoBreakingTask extends BukkitRunnable {
    public static List<NoBreakingTask> noBreakingTasks = new ArrayList<>();

    private int timeLeft;
    private Player player;
    public NoBreakingTask(TwitchRewardsPlugin plugin, Player player, int time) {
        timeLeft = time + 1;
        this.player = player;
        runTaskTimer(plugin, 0L, 20L);
        noBreakingTasks.add(this);
    }


    @Override
    public void run() {
        timeLeft--;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                ChatColor.YELLOW + "" + String.format("No Breaking active for " + timeLeft + " seconds")));
        if (timeLeft <= 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.YELLOW + "" + String.format("Breaking blocks re-enabled")));
            cancel();
            noBreakingTasks.remove(this);
        }

    }




    public Player getPlayer() {
        return player;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
