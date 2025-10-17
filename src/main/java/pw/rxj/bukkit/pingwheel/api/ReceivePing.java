package pw.rxj.bukkit.pingwheel.api;

import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
public class ReceivePing {
    private String channel;
    private double x;
    private double y;
    private double z;
    private boolean isEntity;
    private UUID entityId;
    private int sequence;
    private int dimension;

    private ReceivePing(String channel, double x, double y, double z, boolean isEntity, UUID entityId, int sequence, int dimension) {
        this.channel = channel;
        this.x = x;
        this.y = y;
        this.z = z;
        this.isEntity = isEntity;
        this.entityId = entityId;
        this.sequence = sequence;
        this.dimension = dimension;

    }

    public static @Nullable ReceivePing from(FriendlyByteBuf buf) {
        try {
            String channel = buf.readString(PingWheel.MAX_CHANNEL_NAME_LENGTH);

            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();

            boolean isEntity = buf.readBoolean();
            UUID entityId = isEntity ? buf.readUUID() : null;

            int sequence = buf.readInt();
            int dimension = buf.readInt();

            return new ReceivePing(channel, x, y, z, isEntity, entityId, sequence, dimension);
        } catch (Exception e) {
            return null;
        }
    }

    public void stripPlayerEntity() {
        if(!this.isEntity || this.entityId == null) return;

        Optional<? extends Player> player = Bukkit.getServer().getOnlinePlayers().stream().filter(p -> p.getUniqueId().equals(this.entityId)).findFirst();

        if(player.isEmpty()) return;

        this.isEntity = false;
        this.entityId = null;

        return;
    }

    public FriendlyByteBuf asFriendlyByteBuf() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer(0));

        buf.writeString(this.channel);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeBoolean(this.isEntity);
        if(this.isEntity) buf.writeUUID(this.entityId);
        buf.writeInt(this.sequence);
        buf.writeInt(this.dimension);

        return buf;
    }
}
