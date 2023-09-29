package pw.rxj.bukkit.pingwheel.api;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import pw.rxj.bukkit.pingwheel.Main;
import pw.rxj.bukkit.pingwheel.packets.ChannelUpdate;
import pw.rxj.bukkit.pingwheel.packets.ReceivePing;

import java.util.HashMap;
import java.util.UUID;

public class PingWheel implements Listener {
    private static final HashMap<UUID, String> playerChannels = new HashMap<>();

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
        String channel = ping.channel();

        if(!channel.equals(playerChannels.getOrDefault(player.getUniqueId(), ""))) {
            updatePlayerChannel(player, channel);
        }

        for (Player worldPlayer : player.getWorld().getPlayers()) {
            if(!channel.equals(playerChannels.getOrDefault(worldPlayer.getUniqueId(), ""))) continue;

            FriendlyByteBuf originalPacket = ping.originalPacket();
            originalPacket.writeUUID(player.getUniqueId());

            player.sendPluginMessage(plugin, FORWARD_PING_LOCATION, originalPacket.toByteArray());
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
