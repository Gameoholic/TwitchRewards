package com.github.gameoholic.twitchrewards.other;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBarManager {
    public static void displayMessage(Player player, TextComponent textComponent) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
    }
}
