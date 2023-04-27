package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators.AirDrop;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.AirDropTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.swing.*;

public class AirDrop {

    private TwitchRewards plugin;
    private Rarity rarity;
    private AirDropEntity airDropEntity;
    private String redeemActivator;

    public AirDrop(TwitchRewards plugin, Rarity rarity, String redeemActivator) {
        this.rarity = rarity;
        this.plugin = plugin;
        this.redeemActivator = redeemActivator;
    }
    public void spawn(Player player) {
        Location location = player.getLocation();
        location.setY(location.getY() + 10);

        airDropEntity = new AirDropEntity(location, this);

        new AirDropTask(plugin, this);
    }

    public Rarity getRarity() {
        return rarity;
    }

    public AirDropEntity getAirDropEntity() {
        return airDropEntity;
    }
    public String getRedeemActivator() {
        return redeemActivator;
    }
}
