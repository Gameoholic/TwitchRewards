package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GiveItem {
    public static void giveItem(Player player, Material itemMaterial) {
        ItemStack item = new ItemStack(itemMaterial, 1);
        if (!player.getInventory().addItem(item).isEmpty())
            player.getWorld().dropItemNaturally(player.getLocation(), item);
    }
}
