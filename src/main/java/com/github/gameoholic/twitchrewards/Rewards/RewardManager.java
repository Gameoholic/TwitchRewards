package com.github.gameoholic.twitchrewards.rewards;

import com.github.gameoholic.twitchrewards.rewards.rewardactivators.*;
import com.github.gameoholic.twitchrewards.rewards.rewardactivators.airdrop.AirDrop;
import com.github.gameoholic.twitchrewards.rewards.rewardactivators.airdrop.Rarity;
import com.github.gameoholic.twitchrewards.TwitchRewards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RewardManager {

    private final TwitchRewards plugin;
    private Map<String, Map<String, ?>> redeems = new HashMap<>();

    public TwitchRewards getPlugin() {
        return plugin;
    }

    Random rnd;
    public RewardManager(TwitchRewards plugin) {
        this.plugin = plugin;
        rnd = new Random();
    }

    //TODO: Split channel point rewards into different methods
    public void activateChannelPointReward(String streamerUsername, List<String> redeemUsernames,
                                           String activatorUsername, String redeemTitle, int points, String input) {
        if (!redeems.containsKey(redeemTitle))
            return;


        String inputString = "";
        if (input != "" && input != null)
            inputString = " \"" + input + "\"";

        Bukkit.broadcastMessage(interpolateRedeemMessage(plugin.getRedeemMessage(), streamerUsername, activatorUsername,
            redeemTitle, points, inputString));

        Map<String, ?> redeemData = redeems.get(redeemTitle);
        RewardType rewardType = (RewardType) redeemData.get("Reward");

        for (String playerUsername : redeemUsernames) {
            Player player = Bukkit.getPlayer(playerUsername);

            if (player == null)
                continue;

            player.playSound(player.getLocation(), plugin.getSound(), 1.0f, 1.0f);

            switch (rewardType) {
                case SPAWN_ENTITY:
                    EntityType entityType;
                    int count;

                    if (((String)redeemData.get("Entity")).toUpperCase().equals("RANDOM")) {
                        int randomIndex = rnd.nextInt(RandomTypes.randomEntityTypes.length);
                        entityType = RandomTypes.randomEntityTypes[randomIndex];
                    }
                    else
                        entityType = EntityType.valueOf(((String) redeemData.get("Entity")).toUpperCase());

                    if ((redeemData.get("Count").toString().toUpperCase().equals("RANDOM"))) {
                        count = rnd.nextInt(6) + 1;
                    }
                    else
                        count = (int) redeemData.get("Count");

                    for (int i = 0; i < count; i++)
                        SpawnEntity.spawnEntity(player, entityType);
                    break;

                case POTION_EFFECT:
                    PotionEffectType potionEffectType;
                    if (((String)redeemData.get("Effect")).toUpperCase().equals("RANDOM")) {
                        int randomIndex = rnd.nextInt(RandomTypes.randomEffectTypes.length);
                        potionEffectType = RandomTypes.randomEffectTypes[randomIndex];
                    }
                    else
                        potionEffectType = PotionEffectType.getByName(((String) redeemData.get("Effect")).toUpperCase());

                    int duration;
                    if ((redeemData.get("Duration").toString().toUpperCase().equals("RANDOM"))) {
                        duration = (rnd.nextInt(20) + 5) * 20;
                    }
                    else
                        duration = (int) redeemData.get("Duration") * 20;

                    int amplifier;
                    if ((redeemData.get("Duration").toString().toUpperCase().equals("RANDOM"))) {
                        amplifier = rnd.nextInt(101) + 1;
                    }
                    else
                        amplifier = (int) redeemData.get("Amplifier");

                    PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier);
                    PotionEffectR.givePotionEffect(player, potionEffect);
                    break;

                case LAUNCH:
                    int velocity;
                    if (redeemData.get("Velocity").toString().toUpperCase().equals("RANDOM")) {
                        velocity = rnd.nextInt(6) + 1;
                    }
                    else
                        velocity = (int) redeemData.get("Velocity");
                    Launch.launchPlayer(player, velocity);
                    break;

                case DROP_ITEM:
                    DropItem.dropItem(player);
                    break;

                case REMOVE_ITEM:
                    RemoveItem.removeItem(player);
                    break;

                case DROP_INVENTORY:
                    DropInventory.dropInventory(player);
                    break;

                case GIVE_ITEM:
                    Material itemMaterial;
                    if (((String)redeemData.get("Item")).toUpperCase().equals("RANDOM")) {
                        do {
                            Material[] materials = Material.values();
                            itemMaterial = materials[rnd.nextInt(materials.length)];
                        } while (!itemMaterial.isItem() || itemMaterial.isAir()); //Avoid items like AIR

                    }
                    else
                        itemMaterial = Material.matchMaterial(((String) redeemData.get("Item")).toUpperCase());
                    GiveItem.giveItem(player, itemMaterial);
                    break;

                case LOCK_INVENTORY_SLOT:
                    int time;
                    if (redeemData.get("Time").toString().toUpperCase().equals("RANDOM")) {
                        time = rnd.nextInt(120) + 5;
                    }
                    else
                        time = (int) redeemData.get("Time");


                    LockInventorySlot.lockInventorySlot(plugin, player, time);
                    break;

                case CLUTCH_CHALLENGE:
                    Material clutchItemMaterial;
                    if (((String)redeemData.get("ClutchItem")).toUpperCase().equals("RANDOM")) {
                        int randomIndex = rnd.nextInt(RandomTypes.randomClutchItems.length);
                        clutchItemMaterial = RandomTypes.randomClutchItems[randomIndex];
                    }
                    else
                        clutchItemMaterial = Material.getMaterial((String) redeemData.get("ClutchItem"));

                    int height;
                    if (redeemData.get("Height").toString().toUpperCase().equals("RANDOM")) {
                        height = rnd.nextInt(211) + 10;
                    }
                    else
                        height = (int) redeemData.get("Height");

                    ClutchChallenge.activate(player, clutchItemMaterial, height);
                    break;

                case GOD_MODE:
                    int godModeDuration;
                    if (redeemData.get("GodModeDuration").toString().toUpperCase().equals("RANDOM")) {
                        godModeDuration = rnd.nextInt(66) + 5;
                    }
                    else
                        godModeDuration = (int) redeemData.get("GodModeDuration");
                    GodMode.enableGodMode(plugin, player, godModeDuration);
                    break;

                case FLIGHT:
                    int flightDuration;
                    if (redeemData.get("FlightDuration").toString().toUpperCase().equals("RANDOM")) {
                        flightDuration = rnd.nextInt(66) + 5;
                    }
                    else
                        flightDuration = (int) redeemData.get("FlightDuration");
                    Flight.enableFlight(plugin, player, flightDuration);
                    break;

                case NO_CRAFTING:
                    int noCraftingDuration;
                    if (redeemData.get("NoCraftingDuration").toString().toUpperCase().equals("RANDOM")) {
                        noCraftingDuration = rnd.nextInt(211) + 31;
                    }
                    else
                        noCraftingDuration = (int) redeemData.get("NoCraftingDuration");
                    NoCrafting.disableCrafting(plugin, player, noCraftingDuration);
                    break;

                case NO_BREAKING:
                    int noBreakingDuration;
                    if (redeemData.get("NoBreakingDuration").toString().toUpperCase().equals("RANDOM")) {
                        noBreakingDuration = rnd.nextInt(66) + 5;
                    }
                    else
                        noBreakingDuration = (int) redeemData.get("NoBreakingDuration");
                    NoBreaking.disableBreaking(plugin, player, noBreakingDuration);
                    break;

                case NO_PLACING:
                    int noPlacingDuration;
                    if (redeemData.get("NoPlacingDuration").toString().toUpperCase().equals("RANDOM")) {
                        noPlacingDuration = rnd.nextInt(66) + 5;
                    }
                    else
                        noPlacingDuration = (int) redeemData.get("NoPlacingDuration");
                    NoPlacing.disablePlacing(plugin, player, noPlacingDuration);
                    break;

                case AIRDROP:
                    Rarity airDropRarity;
                    if (redeemData.get("AirDropRarity").toString().toUpperCase().equals("RANDOM")) {
                        airDropRarity = Rarity.values()[rnd.nextInt(Rarity.values().length)];
                    }
                    else
                        airDropRarity = Rarity.valueOf(redeemData.get("AirDropRarity").toString().toUpperCase());

                    new AirDrop(plugin, airDropRarity, activatorUsername).spawn(player);
                    break;

                case TNT_RUN:
                    int TNTRunDuration;
                    if (redeemData.get("TNTRunDuration").toString().toUpperCase().equals("RANDOM")) {
                        TNTRunDuration = rnd.nextInt(120) + 5;
                    }
                    else
                        TNTRunDuration = (int) redeemData.get("TNTRunDuration");

                    int TNTRunRadius;
                    if (redeemData.get("TNTRunRadius").toString().toUpperCase().equals("RANDOM")) {
                        TNTRunRadius = rnd.nextInt(6) + 1;
                    }
                    else
                        TNTRunRadius = (int) redeemData.get("TNTRunRadius");

                    double blockDisappearDelay;
                    if (redeemData.get("BlockDisappearDelay").toString().toUpperCase().equals("RANDOM")) {
                        blockDisappearDelay = rnd.nextFloat(4) + 0.2;
                    }
                    else
                        blockDisappearDelay = (double) redeemData.get("BlockDisappearDelay");

                    TNTRun.activate(plugin, player, TNTRunDuration, blockDisappearDelay, TNTRunRadius);
                    break;
            }
        }

        //Rewards that don't need to be handed out to every player (world-based)
        switch (rewardType) {
            case ADD_TO_WHITELIST:
                int whitelistDuration;
                    if (redeemData.get("WhitelistDuration").toString().toUpperCase().equals("RANDOM")) {
                        whitelistDuration = rnd.nextInt(481) + 120;
                    }
                else
                    whitelistDuration = (int) redeemData.get("WhitelistDuration");

                int teleportCooldown;
                if (redeemData.get("TeleportCooldown").toString().toUpperCase().equals("RANDOM")) {
                    teleportCooldown = rnd.nextInt(125) + 5;
                }
                else
                    teleportCooldown = (int) redeemData.get("TeleportCooldown");

                Whitelist.addToWhitelist(plugin, input.substring(0, Math.min(input.length(), 16)),
                    redeemUsernames, whitelistDuration, teleportCooldown);
                break;
        }


    }

    private String interpolateRedeemMessage(String redeemMessage, String streamerUsername, String activatorUsername,
                                            String redeemTitle, int points, String input) {
        redeemMessage = ChatColor.translateAlternateColorCodes('&', redeemMessage)
            .replaceAll("STREAMER_USERNAME", streamerUsername)
            .replaceAll("ACTIVATOR_USERNAME", activatorUsername)
            .replaceAll("REDEEM_TITLE", redeemTitle)
            .replaceAll("POINTS", points + "")
            .replaceAll("MESSAGE", input);

        return redeemMessage;
    }

    public void addRedeem(String redeemID, Map<String, Object> redeemData) {
        redeems.put(redeemID, redeemData);
    }

    public Map<String, Map<String, ?>> getRedeems() {
        return redeems;
    }

    public RewardType parseRewardType(String rewardType) {
        switch (rewardType) {
            case "SpawnEntity":
                return RewardType.SPAWN_ENTITY;
            case "PotionEffect":
                return RewardType.POTION_EFFECT;
            case "Launch":
                return RewardType.LAUNCH;
            case "DropItem":
                return RewardType.DROP_ITEM;
            case "RemoveItem":
                return RewardType.REMOVE_ITEM;
            case "DropInventory":
                return RewardType.DROP_INVENTORY;
            case "GiveItem":
                return RewardType.GIVE_ITEM;
            case "LockInventorySlot":
                return RewardType.LOCK_INVENTORY_SLOT;
            case "ClutchChallenge":
                return RewardType.CLUTCH_CHALLENGE;
            case "GodMode":
                return RewardType.GOD_MODE;
            case "Flight":
                return RewardType.FLIGHT;
            case "NoCrafting":
                return RewardType.NO_CRAFTING;
            case "NoBreaking":
                return RewardType.NO_BREAKING;
            case "NoPlacing":
                return RewardType.NO_PLACING;
            case "AddToWhitelist":
                return RewardType.ADD_TO_WHITELIST;
            case "AirDrop":
                return RewardType.AIRDROP;
            case "TNTRun":
                return RewardType.TNT_RUN;
            default:
                throw new RuntimeException(String.format("Reward type %s wasn't found.", rewardType));
        }
    }



}
