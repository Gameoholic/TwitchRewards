package com.github.gameoholic.twitchrewards.listeners;

import com.github.gameoholic.twitchrewards.tasks.WhitelistTask;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PlayerJoinListener implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Optional<WhitelistTask> task = WhitelistTask.whitelistTasks.stream()
                .filter(t -> t.getPlayerUsername().equalsIgnoreCase(e.getPlayer().getName()))
                .findFirst();
        if (task.isPresent()) {
            WhitelistTask whitelistTask = task.get();
            Player player = e.getPlayer();
            Player affectedPlayer = whitelistTask.getAffectedPlayer();

            player.teleport(whitelistTask.getAffectedPlayer());
            player.setBedSpawnLocation(whitelistTask.getAffectedPlayer().getLocation());

            if (!whitelistTask.hasJoined()) {
                for (Player onlinePlayer: Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0f, 1.0f);
                }
                affectedPlayer.spawnParticle(Particle.FLAME,
                        affectedPlayer.getLocation().getX(), affectedPlayer.getLocation().getY(),
                        affectedPlayer.getLocation().getZ(), 50, 0, 0.2, 0, 0.2);
                whitelistTask.setJoined(true);
            }

        }



    }
}
