package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClutchChallenge {
    public static void activate(Player player, Material material, int height) {
        Location location = player.getLocation();
        location.setY(location.getY() + height);
        player.teleport(location);
        player.dropItem(true);
        player.getInventory().setItemInMainHand(null);
        player.getInventory().setItemInMainHand(new ItemStack(material, 1));
    }
}
