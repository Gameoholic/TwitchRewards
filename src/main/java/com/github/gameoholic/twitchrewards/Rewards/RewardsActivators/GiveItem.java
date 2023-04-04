package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItem {
    public static void giveItem(Player player, Material itemMaterial) {
        ItemStack item = new ItemStack(itemMaterial, 1);
        player.getWorld().dropItemNaturally(player.getLocation(), item);
    }
}
