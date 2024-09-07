package pl.supereasy.sectors.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class EncoderHelper {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

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
        return UUID.fromString(readString(buf));
    }
}
