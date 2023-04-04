package com.github.gameoholic.twitchrewardsplugin.Twitch;

import com.github.gameoholic.twitchrewardsplugin.TwitchRewardsPlugin;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.pubsub.PubSubSubscription;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class TwitchManager {

    private TwitchRewardsPlugin plugin;
    private TwitchClient twitchClient = null;
    private PubSubSubscription channelPointEventSub;
    private String streamerUsername;
    private String streamerID;
    private OAuth2Credential oAuth;
    private String clientId;
    private String accessToken;
    public TwitchManager(TwitchRewardsPlugin plugin) {
        this.plugin = plugin;
    }


    //gp762nuuoqcoxypju8c569th9wz7q5 - client id
    //5fnlxikru15qm1alh8mjeq4o4ycgcq - access token
    public void startClient(CommandSender commandSender) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] Starting Twitch client, please wait...");

        plugin.setPlayerUsernames(plugin.getConfig().getStringList("RedeemPlayers"));
        accessToken = plugin.getConfig().getString("AccessToken");
        clientId = plugin.getConfig().getString("ClientID");
        streamerUsername = plugin.getConfig().getString("StreamerUsername");

        try {
            oAuth = new OAuth2Credential("twitch", accessToken);

            twitchClient = TwitchClientBuilder.builder()
                    .withClientId(clientId)
                    .withChatAccount(oAuth)
                    .withEnablePubSub(true)
                    .withEnableChat(true)
                    .withEnableHelix(true)
                    .build();

            translateStreamerUsernameToID();

            listenToRedeems();
        }
        catch (Exception e) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards]" +
                    ChatColor.RED + "Couldn't start Twitch Client. Error code: " + ChatColor.AQUA + e.getMessage());
            stopClient();
            return;
        }

        Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards]" +
                ChatColor.GREEN + "Successfully started Twitch Client. Listening for redeems on " +
                ChatColor.YELLOW + streamerUsername + ChatColor.AQUA + " (" + streamerID + "). " + ChatColor.YELLOW +
                "Affecting player/s " + plugin.getPlayerUsernames().toString() + ".");

    }

    public void translateStreamerUsernameToID() {
        boolean foundStreamer = true;
        try {
            UserList resultList = twitchClient.getHelix().getUsers(oAuth.getAccessToken(),
                    null, Arrays.asList(streamerUsername)).execute();

            resultList.getUsers().forEach(user -> {
                streamerID = user.getId();
            });
            if (resultList.getUsers().isEmpty())
                foundStreamer = false;
        }
        catch (Exception e) {
            throw new RuntimeException("Error searching for Streamer with username " + streamerUsername + ".");
        }
        if (!foundStreamer)
            throw new RuntimeException("Couldn't find Streamer with username " + streamerUsername + ".");




    }
    private void listenToRedeems() {
        twitchClient.getChat().joinChannel(streamerUsername);

        channelPointEventSub = twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(oAuth, streamerID);
        twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, event -> {
            onRewardRedeemedEvent(event);
        });


    }

    private void onRewardRedeemedEvent(RewardRedeemedEvent e) {
        ChannelPointsRedemption redemption = e.getRedemption();

        plugin.getServer().getScheduler().runTask(plugin, () -> { //Can't run asynchronously
            plugin.getRewardManager().
                    activateChannelPointReward(redemption.getUser().getDisplayName(),
                            redemption.getReward().getTitle(), (int) redemption.getReward().getCost(),
                            redemption.getUserInput());
        });

    }

    public void stopClient() {
        //twitchClient.getPubSub().unsubscribeFromTopic(channelPointEventSub);
        //twitchClient.getPubSub().disconnect();
        //twitchClient.getPubSub().close();
        if (twitchClient != null) {
            twitchClient.close();
            twitchClient.getEventManager().close();
        }
        twitchClient = null;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void setStreamerUsername(String streamerUsername) {
        this.streamerUsername = streamerUsername;
    }
    public TwitchClient getTwitchClient() {
        return twitchClient;
    }
}
