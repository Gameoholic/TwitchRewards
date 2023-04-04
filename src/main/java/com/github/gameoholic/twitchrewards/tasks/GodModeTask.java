package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GodModeTask extends BukkitRunnable {
    public static List<GodModeTask> godModeTasks = new ArrayList<>();

    private int timeLeft;
    private Player player;
    public GodModeTask(TwitchRewards plugin, Player player, int time) {
        timeLeft = time + 1;
        this.player = player;
        runTaskTimer(plugin, 0L, 20L);
        godModeTasks.add(this);
    }


    @Override
    public void run() {
        timeLeft--;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                ChatColor.YELLOW + "" + String.format("God mode active for " + timeLeft + " seconds")));
        if (timeLeft <= 0) {
            player.setInvulnerable(false);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.YELLOW + "" + String.format("God mode deactivated")));
            cancel();
            godModeTasks.remove(this);
        }

    }




    public Player getPlayer() {
        return player;
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
