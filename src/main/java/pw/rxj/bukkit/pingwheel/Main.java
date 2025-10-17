package pw.rxj.bukkit.pingwheel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import pw.rxj.bukkit.pingwheel.api.FriendlyByteBuf;
import pw.rxj.bukkit.pingwheel.api.PingWheel;
import pw.rxj.bukkit.pingwheel.packets.ChannelUpdate;
import pw.rxj.bukkit.pingwheel.packets.ReceivePing;
import pw.rxj.bukkit.pingwheel.util.PrefixedLogger;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {
    public static final String MOD_ID = "pingwheel-plugin";
    public static final Logger LOGGER = Logger.getLogger("Ping Wheel-Plugin");
    public static final String PLUGIN_ID = "pingwheel-plugin";
    public static final PrefixedLogger LOGGER = new PrefixedLogger(PLUGIN_ID);

    public final PluginManager pluginManager = getServer().getPluginManager();
    public final Messenger messenger = getServer().getMessenger();

    public final PingWheel pingWheel = new PingWheel();

    @Override
    public void onEnable() {
        messenger.registerIncomingPluginChannel(this, PingWheel.RECEIVE_PING_LOCATION, (channel, player, data) -> {
            ByteBuf buf = Unpooled.buffer(0); buf.writeBytes(data);
            ReceivePing receivePing = ReceivePing.from(new FriendlyByteBuf(buf));

            if(receivePing == null) {
                player.sendMessage(ChatColor.RED + "[Ping-Wheel] Ping couldn't be sent. (Incompatible Version?)");
            } else {
                pingWheel.onPingLocation(player, receivePing, this);
            }
        });

        messenger.registerIncomingPluginChannel(this, PingWheel.UPDATE_CHANNEL, (channel, player, data) -> {
            ByteBuf buf = Unpooled.buffer(0); buf.writeBytes(data);
            ChannelUpdate channelUpdate = ChannelUpdate.from(new FriendlyByteBuf(buf));

            if(channelUpdate == null) {
                player.sendMessage(ChatColor.RED + "[Ping-Wheel] Channel couldn't be changed. (Incompatible Version?)");
            } else {
                pingWheel.onChannelUpdate(player, channelUpdate);
            }
        });

        messenger.registerOutgoingPluginChannel(this, PingWheel.FORWARD_PING_LOCATION);

        pluginManager.registerEvents(new PingWheel(), this);
    }

    @Override
    public void onDisable() {
        messenger.unregisterIncomingPluginChannel(this);
        messenger.unregisterOutgoingPluginChannel(this);
    }
}
