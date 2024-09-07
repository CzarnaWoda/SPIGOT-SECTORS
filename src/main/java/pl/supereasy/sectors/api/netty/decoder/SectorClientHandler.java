package pl.supereasy.sectors.api.netty.decoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.api.packets.impl.PacketHandlerImpl;
import pl.supereasy.sectors.api.packets.impl.conn.RegisterRequestPacket;
import pl.supereasy.sectors.api.packets.impl.conn.SectorRegisterPacket;
import pl.supereasy.sectors.api.packets.impl.synchro.UserChangeSectorPacket;

import java.util.HashSet;
import java.util.Set;

public class SectorClientHandler extends SimpleChannelInboundHandler<Packet> {

    private final PacketHandler packetHandler = new PacketHandlerImpl(SectorPlugin.getInstance());
    private Channel channel;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {

        packet.handlePacket(this.packetHandler);
        //this.channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE); nie odsylamy nic ze spigota po przeczytaniu ;]
    }

    public boolean isDisconnected() {
        return getChannel() == null || !getChannel().isOpen() || !getChannel().isActive();
    }

    public Channel getChannel() {
        return channel;
    }
}
