package com.github.gameoholic.twitchrewards.rewards.rewardactivators;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.TNTRunTask;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TNTRun {

    public static void activate(TwitchRewards plugin, Player player, int TNTRunDuration, double blockDisappearDelay, int TNTRunRadius) {

        //No duplicate tasks. If new, just change existing one's time
        Optional<TNTRunTask> task = TNTRunTask.TNTRunTasks.stream()
            .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
            .findFirst();

        if (task.isPresent()) {
            task.get().setRadius(TNTRunRadius);
            task.get().setTimeLeft(TNTRunDuration);
            task.get().setBlockDisappearDelay((blockDisappearDelay));
        }
        else
            new TNTRunTask(plugin, player, TNTRunDuration, blockDisappearDelay, TNTRunRadius);
    }
}
