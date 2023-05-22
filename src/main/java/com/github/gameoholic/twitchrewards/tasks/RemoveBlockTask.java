package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RemoveBlockTask extends BukkitRunnable {
    public static List<RemoveBlockTask> removeBlockTasks = new ArrayList<>();
    private Block block;
    private Material type;
    private int radius;
    public RemoveBlockTask(TwitchRewards plugin, int delay, Block block, int radius) {
        removeBlockTasks.add(this);
        this.block = block;
        this.radius = radius;
        this.type = block.getType();
        runTaskTimer(plugin, delay, delay);
    }

    @Override
    public void run() {

        int minX = block.getX() - radius + 1;
        int minZ = block.getZ() - radius + 1;
        int maxX = block.getX() + radius - 1;
        int maxZ = block.getZ() + radius - 1;



        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Location location = new Location(block.getWorld(), x, block.getY(), z);
                Block currentBlock = location.getBlock();
                if (!currentBlock.isEmpty() && !currentBlock.isLiquid() && currentBlock.getType().equals(type)) {
                    currentBlock.setType(Material.AIR);
                }
            }
        }

        cancel();
        removeBlockTasks.remove(this);
    }

    public Block getBlock() {
        return block;
    }
}
