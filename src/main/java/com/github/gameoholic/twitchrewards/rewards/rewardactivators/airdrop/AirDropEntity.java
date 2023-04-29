package com.github.gameoholic.twitchrewards.rewards.rewardactivators.airdrop;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AirDropEntity {
    private AirDrop airDrop;
    private ArmorStand armorStand;
    private List<Chicken> chickens;
    private List<Zombie> zombies;
    public AirDropEntity(Location location, AirDrop airDrop) {
        this.airDrop = airDrop;
        zombies = new ArrayList<>();

        location.setX(location.getBlockX() - 0.5);
        location.setZ(location.getBlockZ() - 0.5);
        location.setDirection(new Vector(0, 0, -1));

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setInvisible(true);

        ItemStack item = new ItemStack(Material.CHEST, 1);
        armorStand.getEquipment().setHelmet(item);

        chickens = new ArrayList<>();
        Random rnd = new Random();

        int chickensAmount = 0;
        boolean addJockey = false;
        switch (airDrop.getRarity()) {
            case COMMON:
                chickensAmount = 1;
                break;
            case UNCOMMON:
                chickensAmount = 3;
                break;
            case RARE:
                chickensAmount = 5;
                break;
            case EPIC:
                chickensAmount = 8;
                break;
            case LEGENDARY:
                chickensAmount = 10;
                addJockey = true;
                break;
        }

        List<Location> chickenLocations = new ArrayList<>();
        double radius = rnd.nextFloat(5) + 1.5;
        for (int j = 0; j < chickensAmount; j++) {
            double angle = 2 * Math.PI * j / chickensAmount;
            double circleX = (radius / 2) * Math.cos(angle);
            double circleZ = (radius / 2) * Math.sin(angle);
            Location newLocation = location.clone();
            newLocation.setX(newLocation.getX() + circleX);
            newLocation.setZ(newLocation.getZ() + circleZ);
            chickenLocations.add(newLocation);
        }

        for (int i = 0; i < chickensAmount; i++) {
            Location chickenLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            if (chickensAmount == 20) {
                chickenLoc.setX(chickenLoc.getX() + rnd.nextFloat(7) - 3);
                chickenLoc.setZ(chickenLoc.getZ() + rnd.nextFloat(7) - 3);
            }
            else if (chickensAmount != 1)
                chickenLoc = chickenLocations.get(i);
            chickenLoc.setY(chickenLoc.getY() + 5);


            Chicken chicken = (Chicken) location.getWorld().spawnEntity(chickenLoc, EntityType.CHICKEN);
            if (addJockey) {
                Zombie zombie = (Zombie) location.getWorld().spawnEntity(chickenLoc, EntityType.ZOMBIE);
                zombie.setInvulnerable(true);
                zombie.setBaby(true);
                ItemStack crown = new ItemStack(Material.GOLDEN_HELMET, 1);
                zombie.getEquipment().setHelmet(crown);
                chicken.addPassenger(zombie);
                zombies.add(zombie);
            }

            chicken.setInvulnerable(true);
            chicken.setLeashHolder(armorStand);

            chickens.add(chicken);
        }

    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }
    public List<Chicken> getChickens() {
        return chickens;
    }
    public List<Zombie> getZombies() {
        return zombies;
    }
}
