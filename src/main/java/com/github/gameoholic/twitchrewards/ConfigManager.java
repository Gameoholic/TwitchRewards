package com.github.gameoholic.twitchrewards;

import com.github.gameoholic.twitchrewards.Rewards.RewardType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {

    TwitchRewards plugin;

    public ConfigManager(TwitchRewards plugin) {
        this.plugin = plugin;
    }
    public void loadConfig() {
        Bukkit.getLogger().log(Level.INFO, "Loading config file...");
        try {
            for (Object redeemObject : plugin.getRewardManager().getPlugin().getConfig().getList("Redeems")) {
                if (!(redeemObject instanceof Map))
                    throw new RuntimeException("Invalid YML formatting.");
                Map<?, ?> redeemMap = (Map<?, ?>) redeemObject;


                for (Object redeemID : redeemMap.keySet()) {
                    //For each redeem:
                    Object redeemDataListObj = redeemMap.get(redeemID);

                    String redeemIDString = redeemID + "";
                    Map<String, Object> redeemData_RewardManager = new HashMap<>();
                    RewardType rewardType = null;

                    if (!(redeemDataListObj instanceof List))
                        throw new RuntimeException("Invalid YML formatting.");
                    List<?> redeemDataList = (List<?>) redeemDataListObj;
                    for (Object redeemDataObj : redeemDataList) {
                        Map<String, ?> redeemData = (Map<String, ?>)redeemDataObj;

                        //For each redeem data key-value pair
                        //Redeem data examples: {"reward": "SpawnEntity"} / {"Cost": 300}
                        for (String redeemDataKey : redeemData.keySet()) {
                            Object redeemDataValue = redeemData.get(redeemDataKey);
                            switch (redeemDataKey) {
                                case "Reward":
                                    validateRewardDataKey();
                                    rewardType = plugin.getRewardManager().parseRewardType((String) redeemDataValue);
                                    redeemData_RewardManager.put("Reward", rewardType);
                                    break;
                                case "Entity":
                                    validateEntityDataKey(rewardType, (String) redeemDataValue);
                                    redeemData_RewardManager.put("Entity", redeemDataValue);
                                    break;
                                case "Effect":
                                    validateEffectDataKey(rewardType);
                                    redeemData_RewardManager.put("Effect", redeemDataValue);
                                    break;
                                case "Duration":
                                    validateDurationDataKey(rewardType);
                                    redeemData_RewardManager.put("Duration", redeemDataValue);
                                    break;
                                case "Amplifier":
                                    validateAmplifierDataKey(rewardType);
                                    redeemData_RewardManager.put("Amplifier", redeemDataValue);
                                    break;
                                case "Velocity":
                                    validateVelocityDataKey(rewardType);
                                    redeemData_RewardManager.put("Velocity", redeemDataValue);
                                    break;
                                case "Item":
                                    validateItemDataKey(rewardType);
                                    redeemData_RewardManager.put("Item", redeemDataValue);
                                    break;
                                case "Time":
                                    validateTimeDataKey(rewardType);
                                    redeemData_RewardManager.put("Time", redeemDataValue);
                                    break;
                                case "ClutchItem":
                                    validateClutchItemDataKey(rewardType);
                                    redeemData_RewardManager.put("ClutchItem", redeemDataValue);
                                    break;
                                case "Height":
                                    validateHeightDataKey(rewardType);
                                    redeemData_RewardManager.put("Height", redeemDataValue);
                                    break;
                                case "FlightDuration":
                                    validateFlightDurationDataKey(rewardType);
                                    redeemData_RewardManager.put("FlightDuration", redeemDataValue);
                                    break;
                                case "GodModeDuration":
                                    validateGodModeDurationDataKey(rewardType);
                                    redeemData_RewardManager.put("GodModeDuration", redeemDataValue);
                                    break;
                                case "NoCraftingDuration":
                                    validateNoCraftingDurationDataKey(rewardType);
                                    redeemData_RewardManager.put("NoCraftingDuration", redeemDataValue);
                                    break;
                                case "NoBreakingDuration":
                                    validateNoBreakingDurationDataKey(rewardType);
                                    redeemData_RewardManager.put("NoBreakingDuration", redeemDataValue);
                                    break;
                                case "NoPlacingDuration":
                                    validateNoPlacingDurationDataKey(rewardType);
                                    redeemData_RewardManager.put("NoPlacingDuration", redeemDataValue);
                                    break;
                                case "WhitelistDuration":
                                    validateWhitelistDurationDataKey(rewardType);
                                    redeemData_RewardManager.put("WhitelistDuration", redeemDataValue);
                                    break;
                                case "Count":
                                    validateCountDataKey(rewardType);
                                    redeemData_RewardManager.put("Count", redeemDataValue);
                                    break;
                                default:
                                    break;

                            }
                        }

                    }

                    plugin.getRewardManager().addRedeem(redeemIDString, redeemData_RewardManager);


                }


            }

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.GREEN +
                        "Loaded config file successfully!");
            });

            Bukkit.broadcastMessage(ChatColor.AQUA + "Loaded config file successfully!");
        }
        catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Error loading config file! %s", e.getMessage()));

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] " + ChatColor.RED +
                        "Error loading config file! Check console for more info.");
            });

        }


        for (Map.Entry<String, Map<String, ?>> entry : plugin.getRewardManager().getRedeems().entrySet()) {
            String redeemKey = entry.getKey();
            Map<String, ?> redeemValues = entry.getValue();

            System.out.println("Redeem key: " + redeemKey);
            System.out.println("Redeem values: " + redeemValues.toString());
        }

    }


    private void validateCountDataKey(RewardType rewardType) {
        if (rewardType != RewardType.SPAWN_ENTITY) {
            throw new RuntimeException("Invalid Count argument.");
        }
    }
    private void validateWhitelistDurationDataKey(RewardType rewardType) {
        if (rewardType != RewardType.ADD_TO_WHITELIST) {
            throw new RuntimeException("Invalid Whitelist Duration argument.");
        }
    }
    private void validateNoBreakingDurationDataKey(RewardType rewardType) {
        if (rewardType != RewardType.NO_BREAKING) {
            throw new RuntimeException("Invalid No Breaking Duration argument.");
        }
    }
    private void validateNoPlacingDurationDataKey(RewardType rewardType) {
        if (rewardType != RewardType.NO_PLACING) {
            throw new RuntimeException("Invalid No Placing Duration argument.");
        }
    }
    private void validateNoCraftingDurationDataKey(RewardType rewardType) {
        if (rewardType != RewardType.NO_CRAFTING) {
            throw new RuntimeException("Invalid No Crafting Duration argument.");
        }
    }
    private void validateFlightDurationDataKey(RewardType rewardType) {
        if (rewardType != RewardType.FLIGHT) {
            throw new RuntimeException("Invalid Flight Duration argument.");
        }
    }
    private void validateGodModeDurationDataKey(RewardType rewardType) {
        if (rewardType != RewardType.GOD_MODE) {
            throw new RuntimeException("Invalid God Mode Duration argument.");
        }
    }
    private void validateClutchItemDataKey(RewardType rewardType) {
        if (rewardType != RewardType.CLUTCH_CHALLENGE) {
            throw new RuntimeException("Invalid Clutch Item argument.");
        }
    }
    private void validateHeightDataKey(RewardType rewardType) {
        if (rewardType != RewardType.CLUTCH_CHALLENGE) {
            throw new RuntimeException("Invalid Height argument.");
        }
    }
    private void validateTimeDataKey(RewardType rewardType) {
        if (rewardType != RewardType.LOCK_INVENTORY_SLOT) {
            throw new RuntimeException("Invalid Time argument.");
        }
    }
    private void validateItemDataKey(RewardType rewardType) {
        if (rewardType != RewardType.GIVE_ITEM) {
            throw new RuntimeException("Invalid Item argument.");
        }
    }
    private void validateVelocityDataKey(RewardType rewardType) {
        if (rewardType != RewardType.LAUNCH) {
            throw new RuntimeException("Invalid Velocity argument.");
        }
    }
    private void validateAmplifierDataKey(RewardType rewardType) {
        if (rewardType != RewardType.POTION_EFFECT) {
            throw new RuntimeException("Invalid Amplifier argument.");
        }
    }


    private void validateDurationDataKey(RewardType rewardType) {
        if (rewardType != RewardType.POTION_EFFECT) {
            throw new RuntimeException("Invalid Duration argument.");
        }
    }

    private void validateEffectDataKey(RewardType rewardType) {
        if (rewardType != RewardType.POTION_EFFECT) {
            throw new RuntimeException("Invalid Effect argument.");
        }
    }

    private void validateEntityDataKey(RewardType rewardType, String entityType) {
        if (rewardType != RewardType.SPAWN_ENTITY) {
            throw new RuntimeException(String.format("Required reward type for Entity is SpawnEntity. Provided is %s.", rewardType));
        }
        //Ensure entity type is valid:
        if (!entityType.toUpperCase().equals("RANDOM"))
            EntityType.valueOf(entityType.toUpperCase());
    }

    private void validateRewardDataKey() {

    }



}
