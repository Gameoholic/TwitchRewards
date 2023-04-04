package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class DropInventory {
    public static void dropInventory(Player player) {
        player.dropItem(true);

        for (ItemStack item : player.getInventory()) {
            if (item != null) {
                Entity itemDrop = player.getWorld().dropItemNaturally(player.getLocation(), item);
                Vector velocity = itemDrop.getVelocity();
                itemDrop.setVelocity(velocity.multiply(2));
            }

        }
        player.getInventory().clear();
    }
}
