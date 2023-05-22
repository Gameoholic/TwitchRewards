package com.github.gameoholic.twitchrewards.rewards.rewardactivators;

import org.bukkit.entity.Player;

public class RemoveItem {
    public static void removeItem(Player player) {
        player.getInventory().setItemInMainHand(null);
    }
}
