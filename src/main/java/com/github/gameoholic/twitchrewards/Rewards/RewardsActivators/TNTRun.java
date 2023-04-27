package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.NoPlacingTask;
import com.github.gameoholic.twitchrewards.tasks.TNTRunTask;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TNTRun {

    public static void activate(TwitchRewards plugin, Player player, int TNTRunDuration, double blockDisappearDelay) {

        //No duplicate tasks. If new, just change existing one's time
        Optional<TNTRunTask> task = TNTRunTask.TNTRunTasks.stream()
            .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
            .findFirst();

        if (task.isPresent()) {
            task.get().setTimeLeft(TNTRunDuration);
            task.get().setBlockDisappearDelay((blockDisappearDelay));
        }
        else
            new TNTRunTask(plugin, player, TNTRunDuration, blockDisappearDelay);
    }
}
