package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RemoveBlockTask extends BukkitRunnable {
    public static List<RemoveBlockTask> removeBlockTasks = new ArrayList<>();
    private Block block;
    public RemoveBlockTask(TwitchRewards plugin, int delay, Block block) {
        removeBlockTasks.add(this);
        this.block = block;
        runTaskTimer(plugin, delay, delay);
    }

    @Override
    public void run() {
        if (!block.isEmpty() && !block.isLiquid())
            block.setType(Material.AIR);
        cancel();
        removeBlockTasks.remove(this);
    }

    public Block getBlock() {
        return block;
    }
}
