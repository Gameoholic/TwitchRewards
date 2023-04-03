package com.github.gameoholic.twitchrewardsplugin.Rewards.RewardsActivators;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import com.github.gameoholic.twitchrewardsplugin.tasks.InventorySlotLockerTask;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Optional;

public class LockInventorySlot {
    public static void lockInventorySlot(TwitchRewardsPlugin plugin, Player player, int time) {
        int slot = player.getInventory().getHeldItemSlot();
        player.getInventory().setHeldItemSlot(slot);


        //No duplicate tasks. If new, just change existing one's time
        Optional<InventorySlotLockerTask> task = InventorySlotLockerTask.inventorySlotLockerTasks.stream()
                .filter(t -> t.getPlayer() != null && t.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst();

        if (task.isPresent())
            task.get().setTimeLeft(((time + 1) * 20 + 1));
        else
            new InventorySlotLockerTask(plugin, player, time, player.getInventory().getHeldItemSlot());
    }
}
