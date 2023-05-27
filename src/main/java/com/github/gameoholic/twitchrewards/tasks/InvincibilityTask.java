package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.other.ActionBarManager;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InvincibilityTask extends BukkitRunnable {

    public static List<InvincibilityTask> invincibilityTasks = new ArrayList<>();

    private int timeLeft;
    private Player player;
    public InvincibilityTask(TwitchRewards plugin, Player player, int time) {
        timeLeft = time + 1;
        this.player = player;
        runTaskTimer(plugin, 0L, 20L);
        invincibilityTasks.add(this);
    }


    @Override
    public void run() {
        timeLeft--;
        ActionBarManager.displayMessage(player, new TextComponent(
            ChatColor.YELLOW + "" + String.format("Invincibility active for " + timeLeft + " seconds")));
        if (timeLeft <= 0) {
            player.setInvulnerable(false);
            player.setAllowFlight(false);
            ActionBarManager.displayMessage(player, new TextComponent(
                ChatColor.YELLOW + "" + String.format("Invincibility deactivated")));
            cancel();
            invincibilityTasks.remove(this);
        }

    }




    public Player getPlayer() {
        return player;
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

}
