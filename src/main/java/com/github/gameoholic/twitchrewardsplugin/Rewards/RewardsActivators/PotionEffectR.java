package com.github.gameoholic.twitchrewardsplugin.Rewards.RewardsActivators;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectR {
    public static void givePotionEffect(Player player, PotionEffect potionEffectType) {
        player.addPotionEffect(potionEffectType);
    }
}
