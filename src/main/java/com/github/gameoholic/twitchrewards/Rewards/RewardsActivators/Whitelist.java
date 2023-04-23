package com.github.gameoholic.twitchrewards.Rewards.RewardsActivators;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.gameoholic.twitchrewards.tasks.WhitelistTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Whitelist {
    public static void addToWhitelist(TwitchRewards plugin, String playerUsername, List<String> affectedPlayers, int time,
                                      int teleportCooldown) {

        //No duplicate tasks. If new, just change existing one's time
        Optional<WhitelistTask> task = WhitelistTask.whitelistTasks.stream()
                .filter(t -> t.getPlayerUsername() != null && t.getPlayerUsername().equals(playerUsername))
                .findFirst();

        if (task.isPresent())
            task.get().setTimeLeft((time + 1));
        else {
            try {
                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                String command = "whitelist add " + playerUsername;
                Bukkit.dispatchCommand(console, command);
                if (!Bukkit.getWhitelistedPlayers().stream().map(s -> s.getName().toLowerCase()).
                        collect(Collectors.toList()).contains(playerUsername.toLowerCase())) {
                    throw new RuntimeException("Invalid username");
                }
                new WhitelistTask(plugin, playerUsername, affectedPlayers, time, teleportCooldown);
            }
            catch (Exception e) {
                Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] Couldn't add player \"" + playerUsername +
                        "\" to the whitelist.");
                Bukkit.getLogger().log(Level.WARNING, e.getMessage());
            }
        }
    }
}
