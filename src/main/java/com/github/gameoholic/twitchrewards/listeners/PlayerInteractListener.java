package com.github.gameoholic.twitchrewards.listeners;

import com.github.gameoholic.twitchrewards.tasks.NoBreakingTask;
import com.github.gameoholic.twitchrewards.tasks.NoCraftingTask;
import com.github.gameoholic.twitchrewards.tasks.NoPlacingTask;
import com.github.gameoholic.twitchrewards.tasks.WhitelistTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class PlayerInteractListener implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        whitelistCompassUseCheck(e);

        //TODO: Split these into methods as well.

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

    private void whitelistCompassUseCheck(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!(
                (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) &&
                p.getInventory().getItemInMainHand() != null &&
                e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS &&
                p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Teleport to Streamer")))
        return;

        Optional<WhitelistTask> task = WhitelistTask.whitelistTasks.stream()
                .filter(t -> t.getPlayerUsername().equalsIgnoreCase(p.getName()))
                .findFirst();
        if (!task.isPresent())
            return;

        if (task.get().getCompassCooldown() > 0) {
            p.sendMessage(ChatColor.RED + "Teleport is on cooldown for " + task.get().getCompassCooldown()
                    + " seconds.");
            return;
        }

        //We select to teleport to a random player that's ONLINE
        List<Player> onlineRedeemPlayers = task.get().getAffectedPlayers().stream()
                .map(username -> Bukkit.getPlayer(username))
                .filter(player -> player != null)
                .map(player -> Bukkit.getPlayer(player.getName()))
                .collect(Collectors.toList());
        if (onlineRedeemPlayers.size() == 0)
            return;
        Random rnd = new Random();
        int randomIndex = rnd.nextInt(onlineRedeemPlayers.size());
        p.teleport(onlineRedeemPlayers.get(randomIndex));
        task.get().setCompassCooldown(task.get().getMaxCompassCooldown());
    }



}
