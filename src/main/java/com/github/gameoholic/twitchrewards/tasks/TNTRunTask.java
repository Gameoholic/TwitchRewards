package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TNTRunTask extends BukkitRunnable {

    public static List<TNTRunTask> TNTRunTasks = new ArrayList<>();
    private TwitchRewards plugin;
    private int timeLeft;
    private Player player;
    private int blockDisappearDelay;
    private int TNTRunRadius;

    //TODO: fix when redeem ends block will still disappear
    public TNTRunTask(TwitchRewards plugin, Player player, int time, double blockDisappearDelay, int TNTRunRadius) {
        timeLeft = (time + 1) * 20;
        this.plugin = plugin;
        this.player = player;
        this.blockDisappearDelay = (int) (blockDisappearDelay * 20);
        this.TNTRunRadius = TNTRunRadius;
        runTaskTimer(plugin, 0L, 1L);
        TNTRunTasks.add(this);
    }


    @Override
    public void run() {
        timeLeft--;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
            ChatColor.YELLOW + "" + String.format("TNT Run active for " + timeLeft/20 + " seconds")));

        Location underPlayerLoc = player.getLocation();
        underPlayerLoc.setX(underPlayerLoc.getBlockX());
        underPlayerLoc.setY(underPlayerLoc.getBlockY() - 1);
        underPlayerLoc.setZ(underPlayerLoc.getBlockZ());
        if (!underPlayerLoc.getBlock().isEmpty() && !underPlayerLoc.getBlock().isLiquid()) {
            //If block is already marked to be removed, don't mark it again
            Optional<RemoveBlockTask> task = RemoveBlockTask.removeBlockTasks.stream()
                .filter(t -> t.getBlock().getLocation().equals(underPlayerLoc))
                .findFirst();
            if (!task.isPresent())
                new RemoveBlockTask(plugin, blockDisappearDelay, underPlayerLoc.getBlock(), TNTRunRadius);
        }

        if (timeLeft <= 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                ChatColor.YELLOW + "" + String.format("TNT Run ended")));
            cancel();
            TNTRunTasks.remove(this);
        }

    }




    public Player getPlayer() {
        return player;
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = (timeLeft + 1) * 20;
    }
    public void setBlockDisappearDelay(double blockDisappearDelay) {
        this.blockDisappearDelay = (int) (blockDisappearDelay * 20);
    }

    public void setRadius(int TNTRunRadius) {
        this.TNTRunRadius = TNTRunRadius;
    }
}
