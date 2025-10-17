package pw.rxj.bukkit.pingwheel.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;
import pw.rxj.bukkit.pingwheel.Main;
import pw.rxj.bukkit.pingwheel.config.Config;
import pw.rxj.bukkit.pingwheel.records.ChannelUpdate;
import pw.rxj.bukkit.pingwheel.util.ZUtil;

import java.util.HashMap;
import java.util.UUID;

public class PingWheel implements Listener {
    private static final HashMap<UUID, String> playerChannels = new HashMap<>();
    private static final HashMap<UUID, RateLimiter> playerRateLimits = new HashMap<>();

    public static final String UPDATE_CHANNEL = "ping-wheel-c2s:update-channel";
    public static final String RECEIVE_PING_LOCATION = "ping-wheel-c2s:ping-location";
    public static final String FORWARD_PING_LOCATION = "ping-wheel-s2c:ping-location";

    public static final int MAX_CHANNEL_NAME_LENGTH = 128;

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        playerChannels.remove(event.getPlayer().getUniqueId());
    }

    public void onChannelUpdate(Player player, ChannelUpdate update) {
        String channel = update.channel();

        updatePlayerChannel(player, channel);
    }

    public void onPingLocation(Player player, ReceivePing ping, Plugin plugin) {
        UUID playerUUID = player.getUniqueId();

        RateLimiter rateLimiter = playerRateLimits.get(playerUUID);

        if(rateLimiter == null) {
            playerRateLimits.put(playerUUID, new RateLimiter());
        } else if(Main.CONFIG.getRateLimit() > 0 && rateLimiter.checkAndBlock()) {
            return;
        }

        String channel = ping.getChannel();
        Team playerTeam = ZUtil.getPlayerTeam(player);
        Config.DefaultChannel defaultChannelMode = Main.CONFIG.getDefaultChannel();

        if(channel.isEmpty()) {
            if(defaultChannelMode == Config.DefaultChannel.DISABLED) {
                player.sendMessage(ChatColor.DARK_GRAY + "[Ping-Wheel Plugin] " + ChatColor.YELLOW + "Must be in a channel to ping location. " + ChatColor.RESET + "Use " + ChatColor.GREEN + "/pingwheel channel " + ChatColor.RESET + "to switch.");
                return;
            } else if(defaultChannelMode == Config.DefaultChannel.TEAM_ONLY && playerTeam == null) {
                player.sendMessage(ChatColor.DARK_GRAY + "[Ping-Wheel Plugin] " + ChatColor.YELLOW + "Must be in a team or channel to ping location. " + ChatColor.RESET + "Use " + ChatColor.GREEN + "/pingwheel channel " + ChatColor.RESET + "to switch.");
                return;
            }
        }

        if(!Main.CONFIG.isPlayerTrackingAllowed()) ping.stripPlayerEntity();
        FriendlyByteBuf pingPacket = ping.asFriendlyByteBuf();
        pingPacket.writeUUID(playerUUID);

        if(!channel.equals(playerChannels.getOrDefault(playerUUID, ""))) {
            updatePlayerChannel(player, channel);
        }

        for (Player p : player.getWorld().getPlayers()) {
            if(!channel.equals(playerChannels.getOrDefault(p.getUniqueId(), ""))) continue;

            if(defaultChannelMode != Config.DefaultChannel.GLOBAL && !ZUtil.teamEquals(playerTeam, ZUtil.getPlayerTeam(p))) continue;

            p.sendPluginMessage(plugin, FORWARD_PING_LOCATION, pingPacket.toByteArray());
        }
    }

    public void updatePlayerChannel(Player player, String channel) {
        if(channel.isEmpty()) {
            playerChannels.remove(player.getUniqueId());
            Main.LOGGER.info("Channel update: " + String.format("%s -> Global", player.getName()));
        } else {
            playerChannels.put(player.getUniqueId(), channel);
            Main.LOGGER.info("Channel update: " + String.format("%s -> \"%s\"", player.getName(), channel));
        }
    }
}
