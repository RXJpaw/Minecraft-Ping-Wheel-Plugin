package pw.rxj.bukkit.pingwheel.api;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FriendlyByteBuf {
    private final ByteBuf byteBuf;

    public FriendlyByteBuf(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }
    public byte[] toByteArray() {
        byteBuf.resetReaderIndex();

        byte[] byteArray = new byte[this.byteBuf.readableBytes()];
        this.byteBuf.readBytes(byteArray);

        return byteArray;
    }

    public String readString() {
        return readString(Short.MAX_VALUE);
    }
    public String readString(int max) {
        int length = readVarInt();

        if (length > max * 3) return null;
        if (length < 0) return null;

        String string = byteBuf.toString(byteBuf.readerIndex(), length, StandardCharsets.UTF_8);
        byteBuf.readerIndex(byteBuf.readerIndex() + length);
        if (string.length() > max) return null;

        return string;
    }

    public int readInt() {
        return byteBuf.readInt();
    }
    public double readDouble() {
        return byteBuf.readDouble();
    }
    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    public UUID readUUID() {
        return new UUID(byteBuf.readLong(), byteBuf.readLong());
    }
    public void writeUUID(UUID uuid) {
        byteBuf.writeLong(uuid.getMostSignificantBits());
        byteBuf.writeLong(uuid.getLeastSignificantBits());
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b0;
        do {
            b0 = byteBuf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }
}
