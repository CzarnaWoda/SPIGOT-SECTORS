package pl.supereasy.sectors.util.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class NettySerializable {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public abstract void write(final ByteBuf buf);

    public abstract void read(final ByteBuf buf);


    public ByteBuf asBuffer(){
        ByteBuf byteBuf = Unpooled.buffer();
        write(byteBuf);
        return byteBuf;
    }

    public static String readString(ByteBuf buf) {
        short length = buf.readShort();
        byte[] sb = new byte[length];
        buf.readBytes(sb);
        return new String(sb, UTF_8);
    }


    public static void writeString(ByteBuf buf, String s) {
        byte[] sb = s.getBytes(UTF_8);
        buf.writeShort(sb.length);
        buf.writeBytes(sb);
    }

    public static UUID readUUID(ByteBuf buf) {
        return new UUID(buf.readLong(), buf.readLong());
    }

    public static void writeUUID(ByteBuf buf, UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }


}
