package com.github.gameoholic.twitchrewards.tasks;

import com.github.gameoholic.twitchrewards.Rewards.RewardsActivators.AirDrop.AirDrop;
import com.github.gameoholic.twitchrewards.Rewards.RewardsActivators.AirDrop.AirDropEntity;
import com.github.gameoholic.twitchrewards.Rewards.RewardsActivators.AirDrop.AirDropItems;
import com.github.gameoholic.twitchrewards.Rewards.RewardsActivators.AirDrop.Rarity;
import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Item;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AirDropTask extends BukkitRunnable {
    public static List<AirDropTask> airDropTasks = new ArrayList<>();
    private AirDrop airDrop;
    private int particleAnimationFrame = 0;
    private int totalParticleAnimationFrames = 50;
    public AirDropTask(TwitchRewards plugin,  AirDrop airDrop) {
        this.airDrop = airDrop;
        airDropTasks.add(this);
        runTaskTimer(plugin, 0L, 1L);
    }


    @Override
    public void run() {
        AirDropEntity airDropEntity = airDrop.getAirDropEntity();

        Location newLocation = airDropEntity.getArmorStand().getLocation();
        newLocation.setY(newLocation.getY() - 0.1);
        airDropEntity.getArmorStand().teleport(newLocation);

        Location chestLocation = airDropEntity.getArmorStand().getLocation();
        chestLocation.setY(chestLocation.getY() + 1);
        Block chestBlock = chestLocation.getBlock();

        if (particleAnimationFrame == totalParticleAnimationFrames)
            particleAnimationFrame = 0;
        spawnParticle();
        particleAnimationFrame++;

        if (chestBlock.getType().isSolid()) {
            chestLocation.setY(chestLocation.getY() + 1); //We want to place the block above the solid block
            chestBlock = chestLocation.getBlock();
            setChest(chestBlock);

            airDropEntity.getArmorStand().remove();
            for (Chicken chicken: airDropEntity.getChickens()) {
                chicken.remove();
            }
            for (Zombie zombie: airDropEntity.getZombies()) {
                zombie.remove();
            }
            airDropTasks.remove(this);
            cancel();
        }

    }

    private void setChest(Block block) {
        block.setType(Material.CHEST);
        Chest chest = (Chest) block.getState();
        Random rnd = new Random();


        String chestName = "";
        int commonItemsAmount = 0;
        int uncommonItemsAmount = 0;
        int rareItemsAmount = 0;
        int epicItemsAmount = 0;
        int legendaryItemsAmount = 0;
        switch (airDrop.getRarity()) {
            case COMMON:
                commonItemsAmount = rnd.nextInt(4) + 3; //3-6
                uncommonItemsAmount = rnd.nextInt(1); //0-1
                chestName = ChatColor.GRAY + "Common Air Drop";
                break;
            case UNCOMMON:
                commonItemsAmount = rnd.nextInt(3) + 2; //2-4
                uncommonItemsAmount = rnd.nextInt(3) + 2; //2-4
                chestName = ChatColor.GREEN + "Uncommon Air Drop";
                break;
            case RARE:
                commonItemsAmount = rnd.nextInt(4); //0-3
                uncommonItemsAmount = rnd.nextInt(3) + 1; //1-3
                rareItemsAmount = rnd.nextInt(3) + 2; //2-4
                chestName = ChatColor.BLUE + "Rare Air Drop";
                break;
            case EPIC:
                commonItemsAmount = rnd.nextInt(4); //0-3
                uncommonItemsAmount = rnd.nextInt(3) + 3; //3-5
                rareItemsAmount = rnd.nextInt(3) + 2; //2-4
                epicItemsAmount = rnd.nextInt(2) + 1; //1-2
                chestName = ChatColor.DARK_PURPLE + "Epic Air Drop";
                break;
            case LEGENDARY:
                commonItemsAmount = rnd.nextInt(4); //0-3
                uncommonItemsAmount = rnd.nextInt(4) + 2; //2-5
                rareItemsAmount = rnd.nextInt(3) + 2; //2-4
                epicItemsAmount = rnd.nextInt(3) + 1; //1-3
                legendaryItemsAmount = rnd.nextInt(2) + 2; //2-3
                chestName = ChatColor.GOLD + "Legendary Air Drop";
                break;
        }
        //Generate random items:
        List<ItemStack> chestItems = new ArrayList<>();
        int itemsAmount = rnd.nextInt(4) + 3; //3-6 items

        for (int i = 0; i < commonItemsAmount; i++)
            chestItems.add(generateItemStackFromSelection(AirDropItems.commonItems, rnd));
        for (int i = 0; i < uncommonItemsAmount; i++)
            chestItems.add(generateItemStackFromSelection(AirDropItems.uncommonItems, rnd));
        for (int i = 0; i < rareItemsAmount; i++)
            chestItems.add(generateItemStackFromSelection(AirDropItems.rareItems, rnd));
        for (int i = 0; i < epicItemsAmount; i++)
            chestItems.add(generateItemStackFromSelection(AirDropItems.epicItems, rnd));
        for (int i = 0; i < legendaryItemsAmount; i++)
            chestItems.add(generateItemStackFromSelection(AirDropItems.legendaryItems, rnd));

        //Generate random chest item indexes:
        Set<Integer> chestIndexes = new HashSet<>(); //set guarantees they're unique
        while (chestIndexes.size() != chestItems.size()) {
            chestIndexes.add(rnd.nextInt(27));
        }

        //We update chest before putting the items
        chest.setCustomName(chestName + " (" + airDrop.getRedeemActivator() + ")");
        chest.update();

        //Put items in chest:
        int index = 0;
        for (ItemStack itemStack : chestItems) {
            chest.getBlockInventory().setItem(chestIndexes.toArray(new Integer[0])[index], itemStack);
            index++;
        }

    }
    private ItemStack generateItemStackFromSelection(HashMap<Material, Integer> availableItems, Random rnd) {
        Material itemMaterial = (Material) availableItems.keySet().toArray()
            [rnd.nextInt(availableItems.size())];
        int maxQuantity = availableItems.get(itemMaterial);

        int quantity = rnd.nextInt(maxQuantity) + 1;
        ItemStack itemStack = new ItemStack(itemMaterial, quantity);
        return itemStack;
    }

    private void spawnParticle() {
        double radius = 1.5;
        Particle.DustOptions dustOptions = null;
        switch (airDrop.getRarity()) {
            case COMMON:
                dustOptions = new Particle.DustOptions(Color.fromRGB(210, 210, 210), 1.0F);
                break;
            case UNCOMMON:
                dustOptions = new Particle.DustOptions(Color.fromRGB(126, 242, 61), 1.0F);
                break;
            case RARE:
                dustOptions = new Particle.DustOptions(Color.fromRGB(62, 82, 216), 1.0F);
                break;
            case EPIC:
                dustOptions = new Particle.DustOptions(Color.fromRGB(203, 59, 221), 1.0F);
                break;
            case LEGENDARY:
                dustOptions = new Particle.DustOptions(Color.fromRGB(209, 137, 33), 1.0F);
                break;
        }

        double angle1 = 2 * Math.PI * particleAnimationFrame / totalParticleAnimationFrames;
        double circleX1 = (radius / 2) * Math.cos(angle1);
        double circleZ1 = (radius / 2) * Math.sin(angle1);


        airDrop.getAirDropEntity().getArmorStand().getLocation().getWorld().spawnParticle(
            Particle.REDSTONE,
            new Location(airDrop.getAirDropEntity().getArmorStand().getLocation().getWorld(),
                airDrop.getAirDropEntity().getArmorStand().getLocation().getX() + circleX1, //X
                airDrop.getAirDropEntity().getArmorStand().getLocation().getY() + 1.6, //Y
                airDrop.getAirDropEntity().getArmorStand().getLocation().getZ() + circleZ1), //Z
            10,
            dustOptions
        );



        double angle2 = 2 * Math.PI * (particleAnimationFrame + totalParticleAnimationFrames / 2) / totalParticleAnimationFrames;
        double circleX2 = (radius / 2) * Math.cos(angle2);
        double circleZ2 = (radius / 2) * Math.sin(angle2);

        airDrop.getAirDropEntity().getArmorStand().getLocation().getWorld().spawnParticle(
            Particle.REDSTONE,
            new Location(airDrop.getAirDropEntity().getArmorStand().getLocation().getWorld(),
                airDrop.getAirDropEntity().getArmorStand().getLocation().getX() + circleX2, //X
                airDrop.getAirDropEntity().getArmorStand().getLocation().getY() + 1.6, //Y
                airDrop.getAirDropEntity().getArmorStand().getLocation().getZ() + circleZ2), //Z
            10,
            dustOptions
        );


    }



}
