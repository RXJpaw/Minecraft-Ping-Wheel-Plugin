package pw.rxj.bukkit.pingwheel.packets;

import pw.rxj.bukkit.pingwheel.api.FriendlyByteBuf;
import pw.rxj.bukkit.pingwheel.api.PingWheel;

import java.util.UUID;

public record ReceivePing(String channel, double x, double y, double z, boolean isEntity, UUID uuid, int sequence, FriendlyByteBuf originalPacket) {
    public static ReceivePing from(FriendlyByteBuf buf) {
        try {
            String channel = buf.readString(PingWheel.MAX_CHANNEL_NAME_LENGTH);

            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            boolean isEntity = buf.readBoolean();
            UUID uuid = isEntity ? buf.readUUID() : null;

            int sequence = buf.readInt();

            return new ReceivePing(channel, x, y, z, isEntity, uuid, sequence, buf);
        } catch (Exception e) {
            return null;
        }
    }
}
