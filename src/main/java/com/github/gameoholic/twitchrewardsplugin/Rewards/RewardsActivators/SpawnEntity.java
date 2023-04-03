package com.github.gameoholic.twitchrewardsplugin.Rewards.RewardsActivators;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnEntity {



    public static void spawnEntity(Player player, EntityType entityType) {
        player.getWorld().spawnEntity(player.getLocation(), entityType);
    }
}
