package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.NoCraftingTask;
import org.bukkit.entity.Player;

import java.util.Optional;

public class NoCrafting {
    public static void disableCrafting(TwitchRewards plugin, Player player, int time) {

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
