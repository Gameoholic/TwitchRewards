package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class Launch {
    public static void launchPlayer(Player player, float velocity) {
        Random rnd = new Random();
        Vector vel = new Vector(rnd.nextFloat(2 * velocity + 1) - velocity, rnd.nextFloat(2 * velocity + 1) - velocity, rnd.nextFloat(2 * velocity + 1) - velocity);
        player.setVelocity(vel);
    }
}
