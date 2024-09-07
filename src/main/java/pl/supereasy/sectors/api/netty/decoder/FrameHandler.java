package pl.supereasy.sectors.api.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class FrameHandler extends ByteToMessageCodec<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        final int len = in.readableBytes();
        out.ensureWritable(4 + len);
        out.writeInt(len);
        out.writeBytes(in);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in instanceof EmptyByteBuf || in.readableBytes() < 4) return;
        in.markReaderIndex();
        final int length = in.readInt();
        if (length > in.readableBytes()) {
            in.resetReaderIndex();
            return;
        }
        final ByteBuf packet = ctx.alloc().buffer(length);
        in.readBytes(packet, length);
        out.add(packet);
    }
}
