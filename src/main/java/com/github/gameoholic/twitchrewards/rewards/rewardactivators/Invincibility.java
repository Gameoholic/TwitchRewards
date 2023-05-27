package com.github.gameoholic.twitchrewards.rewards.rewardactivators;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.InvincibilityTask;
import org.bukkit.entity.Player;

import java.util.Optional;

public class Invincibility {
    public static void enableInvincibility(TwitchRewards plugin, Player player, int time) {
        player.setInvulnerable(true);


        //No duplicate tasks. If new, just change existing one's time
        Optional<InvincibilityTask> task = InvincibilityTask.invincibilityTasks.stream()
            .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
            .findFirst();

        if (task.isPresent())
            task.get().setTimeLeft((time + 1));
        else
            new InvincibilityTask(plugin, player, time);
    }
}
