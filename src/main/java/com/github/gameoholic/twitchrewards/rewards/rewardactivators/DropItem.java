package com.github.gameoholic.twitchrewards.rewards.rewardactivators;

import org.bukkit.entity.Player;

public class DropItem {
    public static void dropItem(Player player) {
        player.dropItem(true);
        player.getInventory().setItemInMainHand(null);
    }
}
