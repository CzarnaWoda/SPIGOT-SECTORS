package pl.supereasy.sectors.api.netty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.netty.api.SectorClient;
import pl.supereasy.sectors.api.netty.decoder.SectorClientHandler;
import pl.supereasy.sectors.api.netty.enums.SourceType;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.SourcePacket;
import pl.supereasy.sectors.api.packets.impl.conn.SectorRegisterPacket;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.redis.enums.PubSubType;

import java.util.logging.Level;

public class SectorClientImpl implements SectorClient {

    private final SectorPlugin plugin;
    //private NettyEncoder nettyEncoder;
    private SectorClientHandler client;
    private final Bootstrap b = new Bootstrap();
    private final GsonBuilder gsonBilder = new GsonBuilder();
    private final Gson gson = gsonBilder.create();
    public SectorClientImpl(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void connectAPI() {
        /*EventLoopGroup group = new NioEventLoopGroup();
        b.channel(NioSocketChannel.class)
                .group(group)
                .handler(new ChannelInitializer<Channel>() {
                    public static final String FIELD_PREPENDER = "field-prepender";
                    public static final String PACKET_CODER = "packet-coder";
                    public static final String PACKET_HANDLER = "packet-handler";

                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        try {
                            channel.config().setOption(ChannelOption.IP_TOS, 0x18);
                        } catch (ChannelException ignored) {
                        }
                        channel.config().setAllocator(PooledByteBufAllocator.DEFAULT);
                        channel.pipeline()
                                .addLast(FIELD_PREPENDER, new FrameHandler())
                                .addLast(PACKET_CODER, new PacketEncoderDecoder())
                                .addLast(PACKET_HANDLER, client = new SectorClientHandler());
                    }
                }).option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .connect(plugin.getCurrentSectorConfig().getApiHost(), 25000).syncUninterruptibly();*/
        //reconnectAPI();
    }

    @Override
    public void reconnectAPI() {
        if (!this.client.isDisconnected()) {
            return;
        }
        SectorPlugin.getInstance().getLogger().log(Level.INFO, "Reconnecting to API...!");
        ChannelFuture f = b.connect(SectorPlugin.getInstance().getCurrentSectorConfig().getApiHost(), 25000);
        if (f.isSuccess()) {
            SectorPlugin.getInstance().getLogger().log(Level.INFO, "Reconnected sending SectorRegisterPacket!");
            SectorPlugin.getInstance().getSectorClient().sendPacket(new SectorRegisterPacket(SectorPlugin.getInstance().getSectorManager().getCurrentSector().getSectorName()));
        }
        /*
        if(this.nettyEncoder != null && !this.nettyEncoder.isDisconnected()){
            Bukkit.broadcastMessage("no need reconnect");
            return;
        }
        Bukkit.broadcastMessage("reconnectAPI encoder create!");
        this.nettyEncoder = new NettyEncoder(this.plugin);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(nettyEncoder);
            }
        });
        ChannelFuture f = b.connect("178.32.120.108", 25000);
        if(!f.isSuccess()){
            this.plugin.getLOGGER().log(Level.INFO, "Connect FAILED with API!");
        }
        else{
            this.plugin.getLOGGER().log(Level.INFO, "Connect succesfully with API!");
        }*/
    }

    @Override
    public Channel getChannel() {
        return this.client.getChannel();
    }

    @Override
    public void sendAPIPacket(Packet packet) {
        final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        RedisChannel.INSTANCE.apiTopic.publish(packetID + "@" + gson.toJson(packet));
    }

    @Override
    public void sendGlobalPacket(Packet packet) {
        /*final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        getChannel().writeAndFlush(packet.asBuffer(packetID, SourceType.TO_ALL_SPIGOT));
         */
        final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        this.plugin.getRedisManager().getPubSub(PubSubType.PACKETS).sendMessage(packetID + "@" + gson.toJson(packet));
        //getChannel().writeAndFlush(new SourcePacket(SourceType.TO_ALL_SPIGOT, packet));
    }

    @Override
    public void sendGlobalProxyPacket(Packet packet) {
        /*final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        getChannel().writeAndFlush(packet.asBuffer(packetID, SourceType.PROXY_ALL));
         */
        getChannel().writeAndFlush(new SourcePacket(SourceType.PROXY_ALL, packet));
    }

    @Override
    public void sendPacket(Packet packet) {
        //final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        //getChannel().writeAndFlush(new SourcePacket(SourceType.TO_ALL_SPIGOT, packet));
        final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        this.plugin.getRedisManager().getPubSub(PubSubType.PACKETS).sendMessage(packetID + "@" + gson.toJson(packet));
        //TODO
    }

    @Override
    public void sendPacket(Packet packet, SourceType sourceType) {
        //final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        getChannel().writeAndFlush(new SourcePacket(sourceType, packet));
    }

    @Override
    public void sendPacket(Packet packet, String sectorName) {
        final int packetID = this.plugin.getPacketManager().getPacketID(packet.getClass());
        //getChannel().writeAndFlush(new SourcePacket(SourceType.TO_SPIGOT, sectorName, packet));
        this.plugin.getRedisManager().getPubSub(PubSubType.PACKET_TO_SECTOR).sendMessage(sectorName + "#" + packetID + "@" + gson.toJson(packet));
    }

    @Override
    public void sendUser(Packet packet, String sectorName) {

        this.plugin.getRedisManager().getPubSub(PubSubType.USER).sendMessage(sectorName, packet);
    }
}
