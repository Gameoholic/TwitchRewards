package com.github.gameoholic.twitchrewards.rewards.rewardactivators;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClutchChallenge {
    public static void activate(Player player, Material material, int height) {
        Location location = player.getLocation();
        Location newLoc = location;
        for (int i = 1; i <= height + 2; i++) {
            newLoc.setY(location.getY() + 1);
            if (newLoc.getBlock().getType() != Material.AIR) {
                break;
            }
        }
        newLoc.setY(newLoc.getY() - 2);
        player.teleport(newLoc);

        player.teleport(location);
        player.dropItem(true);
        player.getInventory().setItemInMainHand(null);
        player.getInventory().setItemInMainHand(new ItemStack(material, 1));
    }
}
