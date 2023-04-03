package com.github.gameoholic.twitchrewardsplugin.Rewards.RewardsActivators;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DropItem {
    public static void dropItem(Player player) {
        player.dropItem(true);
        player.getInventory().setItemInMainHand(null);
    }
}
