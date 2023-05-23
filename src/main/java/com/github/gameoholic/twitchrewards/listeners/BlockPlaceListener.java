package com.github.gameoholic.twitchrewards.listeners;

import com.github.gameoholic.twitchrewards.tasks.NoPlacingTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        Optional<NoPlacingTask> task = NoPlacingTask.noPlacingTasks.stream()
            .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId()))
            .findFirst();
        if (task.isPresent())
            e.setCancelled(true);
    }




}
