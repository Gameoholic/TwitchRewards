package com.github.gameoholic.twitchrewardsplugin.Rewards.RewardsActivators;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import com.github.gameoholic.twitchrewardsplugin.tasks.FlightTask;
import com.github.gameoholic.twitchrewardsplugin.tasks.NoCraftingTask;
import org.bukkit.entity.Player;

import java.util.Optional;

public class NoCrafting {
    public static void disableCrafting(TwitchRewardsPlugin plugin, Player player, int time) {

        //No duplicate tasks. If new, just change existing one's time
        Optional<NoCraftingTask> task = NoCraftingTask.noCraftingTasks.stream()
                .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst();

        if (task.isPresent())
            task.get().setTimeLeft((time + 1));
        else
            new NoCraftingTask(plugin, player, time);
    }
}
