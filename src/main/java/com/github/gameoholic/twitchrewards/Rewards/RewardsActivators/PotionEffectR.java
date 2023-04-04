package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class PotionEffectR {
    public static void givePotionEffect(Player player, PotionEffect potionEffectType) {
        player.addPotionEffect(potionEffectType);
    }
}
