package org.cptgummiball.isstreamerlive;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin implements Listener {

    private List<Map<String, String>> streamers;
    private int checkInterval;
    private final String clientId = getConfig().getString("APIClientID");
    private final String accessToken = getConfig().getString("TwitchAccessToken");
    private final Map<String, Boolean> liveStatus = new HashMap<>();  // Status der Streamer (ob sie live sind)

    @Override
    public void onEnable() {
        // Plugin starts
        this.saveDefaultConfig();
        loadConfig();
        startChecking();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin stopped
    }

    private void loadConfig() {
        FileConfiguration config = this.getConfig();
        checkInterval = config.getInt("twitch.check_interval");

        // Use an unsafe type cast, so we need to validate the data
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rawStreamers = (List<Map<String, Object>>) config.getList("twitch.streamers");

        // Initialise Streamer-List
        streamers = rawStreamers.stream()
                .map(entry -> (Map<String, String>) (Map<?, ?>) entry)
                .toList();

        // Initialise Live-Status for every Streamer
        for (Map<String, String> streamer : streamers) {
            liveStatus.put(streamer.get("streamer_name"), false);  // Setze anfangs alle auf "nicht live"
        }
    }

    private void startChecking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Check all Streamers
                for (Map<String, String> streamer : streamers) {
                    String titleText = getConfig().getString("notification_title");
                    String streamerName = streamer.get("streamer_name");
                    String showedName = streamer.get("streamer_showed_name");
                    String notificationType = streamer.get("notification_type");
                    String notificationText = streamer.get("String notification_text");
                    String playedsound = streamer.get("played_sound");

                    boolean isLive = checkIfStreamerIsLive(streamerName);
                    if (isLive && !liveStatus.get(streamerName)) {
                        // Streamer is live now
                        liveStatus.put(streamerName, true);
                        sendNotificationToAllPlayers(showedName, notificationType, titleText, notificationText);
                        playSoundToAllPlayers(playedsound);
                    } else if (!isLive && liveStatus.get(streamerName)) {
                        // Streamer no longer live
                        liveStatus.put(streamerName, false);
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0, checkInterval * 20L);  // Timer im Hintergrund
    }

    private boolean checkIfStreamerIsLive(String streamerName) {
        try {
            String urlString = "https://api.twitch.tv/helix/streams?user_login=" + streamerName;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Client-ID", clientId);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONObject response = new JSONObject(content.toString());
            return response.getJSONArray("data").length() > 0;  // Wenn "data" nicht leer ist, ist der Streamer live

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendNotificationToAllPlayers(String showedName, String notificationType, String titleText, String notificationText) {
        String message = showedName + " " + notificationText;
        if ("chat".equalsIgnoreCase(notificationType)) {
            Bukkit.broadcastMessage(message);
        } else if ("title".equalsIgnoreCase(notificationType)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(titleText, message, 10, 70, 20);
            }
        }
    }

    private void playSoundToAllPlayers(String playedsound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), playedsound, 1, 1);  // Play Sound
        }
    }

    private void sendNotificationToPlayer(Player player, String showedName, String notificationType, String titleText, String notificationText) {
        if (liveStatus.values().contains(true)) {  // Check if a Streamer is live
            String message = showedName + " " + notificationText;
            if ("chat".equalsIgnoreCase(notificationType)) {
                player.sendMessage(message);
            } else if ("title".equalsIgnoreCase(notificationType)) {
                player.sendTitle(titleText, message, 10, 70, 20);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Send message on join event if a streamer is live
        for (Map<String, String> streamer : streamers) {
            String titleText = getConfig().getString("notification_title");
            String notificationText = streamer.get("notification_text");
            String showedName = streamer.get("streamer_showed_name");
            String notificationType = "chat";
            sendNotificationToPlayer(player, showedName, notificationType, titleText, notificationText);
        }
    }
}
