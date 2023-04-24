package com.github.gameoholic.twitchrewards.Twitch;

import com.github.gameoholic.twitchrewards.TwitchRewards;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.pubsub.PubSubSubscription;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class TwitchManager {

    private TwitchRewards plugin;
    private TwitchClient twitchClient = null;
    private OAuth2Credential oAuth;
    private String clientId;
    private String accessToken;
    //List of StreamerUsername, StreamerID pairs, where the values are the MC players whom to affect
    List<HashMap<Pair<String, String>, List<String>>> streamers;
    private List<PubSubSubscription> channelPointEventSubs;

    public TwitchManager(TwitchRewards plugin) {
        this.plugin = plugin;
    }

    public void startClient(CommandSender commandSender) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] Starting Twitch client, please wait...");

        accessToken = plugin.getConfig().getString("AccessToken");
        clientId = plugin.getConfig().getString("ClientID");

        try {
            oAuth = new OAuth2Credential("twitch", accessToken);

            twitchClient = TwitchClientBuilder.builder()
                    .withClientId(clientId)
                    .withChatAccount(oAuth)
                    .withEnablePubSub(true)
                    .withEnableChat(true)
                    .withEnableHelix(true)
                    .build();

            List<HashMap<String, List<String>>> streamersByUsername = plugin.getStreamerList();

            streamers = new ArrayList<>();
            channelPointEventSubs = new ArrayList<>();
            for (HashMap<String, List<String>> streamerByUsername: streamersByUsername) {
                String streamerUsername = (String) streamerByUsername.keySet().toArray()[0];
                String streamerID = translateStreamerUsernameToID(streamerUsername);
                List<String> redeemPlayers = streamerByUsername.get(streamerUsername);

                HashMap<Pair<String, String>, List<String>> streamer = new HashMap<>();
                streamer.put(Pair.of(streamerUsername, streamerID), redeemPlayers);
                streamers.add(streamer);

                listenToRedeems(streamerID, streamerUsername);

                Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] Listening for redeems on " + ChatColor.GREEN +
                    streamerUsername + ChatColor.YELLOW  + " (" + streamerID + "), affecting player/s " + ChatColor.AQUA
                    + redeemPlayers);
            }

            if (streamers.size() > 1)
                Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] " +
                    ChatColor.GREEN + "Successfully started Twitch Client. Listening for redeems on " + ChatColor.AQUA +
                    streamers.size() + ChatColor.GREEN + " different streamers.");
            else
                Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] " +
                    ChatColor.GREEN + "Successfully started Twitch Client. Listening for redeems on " + ChatColor.AQUA +
                    "1" + ChatColor.GREEN + " streamer");
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] " +
                    ChatColor.RED + "Couldn't start Twitch Client. Error: " + ChatColor.AQUA + e.getMessage() +
                ChatColor.RED + "\nCheck console for more details.");
            stopClient();
        }
    }

    public String translateStreamerUsernameToID(String username) {
        try {
            UserList resultList = twitchClient.getHelix().getUsers(oAuth.getAccessToken(),
                    null, Arrays.asList(username)).execute();

            for (User user: resultList.getUsers()) {
                return user.getId();
            }
            throw new RuntimeException("Couldn't find Streamer with username " + username + ".");
        }
        catch (Exception e) {
            throw new RuntimeException("Couldn't find Streamer with username " + username + ".");
        }
    }
    private void listenToRedeems(String streamerID, String streamerUsername) {
        twitchClient.getChat().joinChannel(streamerUsername);
        channelPointEventSubs.add(twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(oAuth, streamerID));
        twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, event -> {
            onRewardRedeemedEvent(event, streamerUsername);
        });


    }

    private void onRewardRedeemedEvent(RewardRedeemedEvent e, String streamerUsername) {
        ChannelPointsRedemption redemption = e.getRedemption();
        //TODO: learn how the fuck this works
        List<String> redeemPlayers = streamers.stream()
            .map(HashMap::entrySet)
            .flatMap(Collection::stream)
            .filter(entry -> entry.getKey().getLeft().equals(streamerUsername))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse(null);
        plugin.getServer().getScheduler().runTask(plugin, () -> { //Can't run asynchronously
            plugin.getRewardManager().
                    activateChannelPointReward(streamerUsername, redeemPlayers, redemption.getUser().getDisplayName(),
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
    public TwitchClient getTwitchClient() {
        return twitchClient;
    }
    public List<HashMap<Pair<String, String>, List<String>>> getStreamers() {
        return streamers;
    }
}
