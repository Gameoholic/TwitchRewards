package com.github.gameoholic.twitchrewards.rewards.rewardactivators;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.NoPlacingTask;
import org.bukkit.entity.Player;

import java.util.Optional;

public class NoPlacing {
    public static void disablePlacing(TwitchRewards plugin, Player player, int time) {

        //No duplicate tasks. If new, just change existing one's time
        Optional<NoPlacingTask> task = NoPlacingTask.noPlacingTasks.stream()
                .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst();

        if (task.isPresent())
            task.get().setTimeLeft((time + 1));
        else
            new NoPlacingTask(plugin, player, time);
    }
}
