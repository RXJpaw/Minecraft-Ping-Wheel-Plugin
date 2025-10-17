package pw.rxj.bukkit.pingwheel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import pw.rxj.bukkit.pingwheel.api.FriendlyByteBuf;
import pw.rxj.bukkit.pingwheel.api.PingWheel;
import pw.rxj.bukkit.pingwheel.api.ReceivePing;
import pw.rxj.bukkit.pingwheel.command.config.ConfigCommand;
import pw.rxj.bukkit.pingwheel.config.ConfigHandler;
import pw.rxj.bukkit.pingwheel.records.ChannelUpdate;
import pw.rxj.bukkit.pingwheel.util.PrefixedLogger;

public final class Main extends JavaPlugin implements Listener {
    public static final String PLUGIN_ID = "pingwheel-plugin";
    public static final PrefixedLogger LOGGER = new PrefixedLogger(PLUGIN_ID);

    public final PluginManager pluginManager = getServer().getPluginManager();
    public final Messenger messenger = getServer().getMessenger();

    public final PingWheel pingWheel = new PingWheel();
    public static ConfigHandler CONFIG = null;


    @Override
    public void onEnable() {
        CONFIG = ConfigHandler.bake(this);
        CONFIG.read();

        messenger.registerIncomingPluginChannel(this, PingWheel.RECEIVE_PING_LOCATION, (channel, player, data) -> {
            ByteBuf buf = Unpooled.buffer(0); buf.writeBytes(data);
            ReceivePing receivePing = ReceivePing.from(new FriendlyByteBuf(buf));

            if(receivePing == null || receivePing.getChannel() == null) {
                player.sendMessage(ChatColor.RED + "[Ping-Wheel] Ping couldn't be sent. (incompatible version?)");
            } else {
                pingWheel.onPingLocation(player, receivePing, this);
            }
        });

        messenger.registerIncomingPluginChannel(this, PingWheel.UPDATE_CHANNEL, (channel, player, data) -> {
            ByteBuf buf = Unpooled.buffer(0); buf.writeBytes(data);
            ChannelUpdate channelUpdate = ChannelUpdate.from(new FriendlyByteBuf(buf));

            if(channelUpdate == null || channelUpdate.channel() == null) {
                player.sendMessage(ChatColor.RED + "[Ping-Wheel] Channel couldn't be updated. (incompatible version?)");
            } else {
                pingWheel.onChannelUpdate(player, channelUpdate);
            }
        });

        messenger.registerOutgoingPluginChannel(this, PingWheel.FORWARD_PING_LOCATION);

        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new PingWheel(), this);

        //config command
        PluginCommand ConfigCommand = this.getCommand("pw_config");
        if(ConfigCommand != null) {
            ConfigCommand command = new ConfigCommand();
            ConfigCommand.setExecutor(command);
            ConfigCommand.setTabCompleter(command);
        }
    }

    @Override
    public void onDisable() {
        messenger.unregisterIncomingPluginChannel(this);
        messenger.unregisterOutgoingPluginChannel(this);
    }
}
