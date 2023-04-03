package com.github.gameoholic.twitchrewardsplugin.Rewards.RewardsActivators;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import com.github.gameoholic.twitchrewardsplugin.tasks.NoBreakingTask;
import com.github.gameoholic.twitchrewardsplugin.tasks.NoCraftingTask;
import com.github.gameoholic.twitchrewardsplugin.tasks.NoPlacingTask;
import org.bukkit.entity.Player;

import java.util.Optional;

public class NoBreaking {
    public static void disableBreaking(TwitchRewardsPlugin plugin, Player player, int time) {

        //No duplicate tasks. If new, just change existing one's time
        Optional<NoBreakingTask> task = NoBreakingTask.noBreakingTasks.stream()
                .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst();

        if (task.isPresent())
            task.get().setTimeLeft((time + 1));
        else
            new NoBreakingTask(plugin, player, time);
    }
}
