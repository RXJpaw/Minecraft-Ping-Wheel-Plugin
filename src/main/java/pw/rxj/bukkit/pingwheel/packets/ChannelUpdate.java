package pw.rxj.bukkit.pingwheel.packets;

import pw.rxj.bukkit.pingwheel.api.FriendlyByteBuf;
import pw.rxj.bukkit.pingwheel.api.PingWheel;


public record ChannelUpdate(String channel, FriendlyByteBuf originalPacket) {
    public static ChannelUpdate from(FriendlyByteBuf buf) {
        try {
            String channel = buf.readString(PingWheel.MAX_CHANNEL_NAME_LENGTH);

            return new ChannelUpdate(channel, buf);
        } catch (Exception e) {
            return null;
        }
    }
}
