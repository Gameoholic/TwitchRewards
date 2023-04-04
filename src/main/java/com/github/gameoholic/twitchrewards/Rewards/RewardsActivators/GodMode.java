package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.GodModeTask;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GodMode {
    public static void enableGodMode(TwitchRewards plugin, Player player, int time) {
        player.setInvulnerable(true);

        //No duplicate tasks. If new, just change existing one's time
        Optional<GodModeTask> task = GodModeTask.godModeTasks.stream()
                .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst();

        if (task.isPresent())
            task.get().setTimeLeft((time + 1));
        else
            new GodModeTask(plugin, player, time);
    }
}
