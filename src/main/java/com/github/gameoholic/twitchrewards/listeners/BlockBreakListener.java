package com.github.gameoholic.twitchrewards.listeners;

import com.github.gameoholic.twitchrewards.tasks.NoBreakingTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Optional<NoBreakingTask> task = NoBreakingTask.noBreakingTasks.stream()
            .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId()))
            .findFirst();
        if (task.isPresent())
            e.setCancelled(true);
    }



}
