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
import java.util.logging.Level;

public class TwitchManager {

    private TwitchRewards plugin;
    private TwitchClient twitchClient = null;
    private OAuth2Credential oAuth;
    private String clientId;
    private String accessToken;
    //List of StreamerUsername, StreamerID pairs, where the values are the MC players whom to affect
    List<HashMap<Pair<String, String>, List<String>>> streamers;
    private PubSubSubscription channelPointEventSub;

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
            List<String> streamerIDs = new ArrayList<>();
            channelPointEventSub = null;
            for (HashMap<String, List<String>> streamerByUsername: streamersByUsername) {
                String streamerUsername = (String) streamerByUsername.keySet().toArray()[0];
                Pair<String, String> streamerInfo = translateStreamerUsernameToID(streamerUsername);
                String streamerID = streamerInfo.getLeft();
                streamerUsername = streamerInfo.getRight(); //Case sensitive - displayname
                List<String> redeemPlayers = streamerByUsername.get(streamerUsername.toLowerCase());

                streamerIDs.add(streamerID);
                
                HashMap<Pair<String, String>, List<String>> streamer = new HashMap<>();
                streamer.put(Pair.of(streamerUsername, streamerID), redeemPlayers);
                streamers.add(streamer);

                Bukkit.broadcastMessage(ChatColor.YELLOW + "[TwitchRewards] Listening for redeems on " + ChatColor.GREEN +
                    streamerUsername + ChatColor.YELLOW  + " (" + streamerID + "), affecting player/s " + ChatColor.AQUA
                    + redeemPlayers);
            }
            listenToRedeems(streamerIDs);

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

    //Returns ID, username. Returns username for it to be case sensitive - displayname
    public Pair<String, String> translateStreamerUsernameToID(String username) {
        try {
            UserList resultList = twitchClient.getHelix().getUsers(oAuth.getAccessToken(),
                    null, Arrays.asList(username)).execute();

            for (User user: resultList.getUsers()) {
                return Pair.of(user.getId(), user.getDisplayName());
            }
            throw new RuntimeException("Couldn't find Streamer with username " + username + ".");
        }
        catch (Exception e) {
            throw new RuntimeException("Couldn't find Streamer with username " + username + ".");
        }
    }
    private void listenToRedeems(List<String> streamerIDs) {
        for (String streamerID: streamerIDs)
            twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(null, streamerID);
        
        twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, event -> {
            onRewardRedeemedEvent(event);
        });
    }

    private void onRewardRedeemedEvent(RewardRedeemedEvent e) {
        ChannelPointsRedemption redemption = e.getRedemption();
        //TODO: learn how the fuck this works
        //Pair of streamer ID, and the redeemPlayers
        Pair<String, List<String>> redeemData = streamers.stream()
            .map(HashMap::entrySet)
            .flatMap(Collection::stream)
            .filter(entry -> entry.getKey().getRight().equals(e.getRedemption().getChannelId()))
            .map(entry -> Pair.of(entry.getKey().getLeft(), entry.getValue()))
            .findFirst()
            .orElse(null);

        String streamerUsername = redeemData.getLeft();
        List<String> redeemPlayers = redeemData.getRight();

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
