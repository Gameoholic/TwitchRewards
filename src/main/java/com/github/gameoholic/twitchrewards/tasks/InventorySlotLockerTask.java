package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InventorySlotLockerTask extends BukkitRunnable {
    public static List<InventorySlotLockerTask> inventorySlotLockerTasks = new ArrayList<>();

    private int timeLeft;

    private int slot;
    private Player player;
    public InventorySlotLockerTask(TwitchRewards plugin, Player player, int time, int slot) {
        timeLeft = (time + 1) * 20 + 1;
        this.player = player;
        this.slot = slot;
        runTaskTimer(plugin, 0L, 1L);
        inventorySlotLockerTasks.add(this);
    }


    @Override
    public void run() {
        timeLeft--;
        player.getInventory().setHeldItemSlot(slot);
        if (timeLeft % 20 != 0)
            return;
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                ChatColor.YELLOW + "" + String.format("Slot locked for " + timeLeft / 20 + " seconds")));
        if (timeLeft <= 0) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.YELLOW + "" + String.format("Inventory slot unlocked")));
            cancel();
            inventorySlotLockerTasks.remove(this);
        }

    }




    public Player getPlayer() {
        return player;
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
    public int getSlot() {
        return slot;
    }
}
