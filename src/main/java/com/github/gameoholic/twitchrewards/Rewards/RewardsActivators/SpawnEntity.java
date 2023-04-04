package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnEntity {



    public static void spawnEntity(Player player, EntityType entityType) {
        player.getWorld().spawnEntity(player.getLocation(), entityType);
    }
}
