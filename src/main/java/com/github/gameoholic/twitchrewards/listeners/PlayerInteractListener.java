package com.github.gameoholic.twitchrewards.listeners;

import com.github.gameoholic.twitchrewards.tasks.NoBreakingTask;
import com.github.gameoholic.twitchrewards.tasks.NoCraftingTask;
import com.github.gameoholic.twitchrewards.tasks.NoPlacingTask;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class PlayerInteractListener implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //No crafting:
        if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CRAFTING_TABLE) {
            Optional<NoCraftingTask> task = NoCraftingTask.noCraftingTasks.stream()
                    .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId()))
                    .findFirst();
            if (task.isPresent())
                e.setCancelled(true);
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Optional<NoBreakingTask> task = NoBreakingTask.noBreakingTasks.stream()
                    .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId()))
                    .findFirst();
            if (task.isPresent())
                e.setCancelled(true);
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Optional<NoPlacingTask> task = NoPlacingTask.noPlacingTasks.stream()
                    .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId()))
                    .findFirst();
            if (task.isPresent())
                e.setCancelled(true);
        }
    }
}
