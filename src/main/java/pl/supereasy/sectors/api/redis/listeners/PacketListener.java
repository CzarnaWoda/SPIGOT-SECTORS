package pl.supereasy.sectors.api.redis.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;
import pl.supereasy.sectors.api.packets.impl.PacketHandlerImpl;
import pl.supereasy.sectors.api.redis.api.RedisListener;
import pl.supereasy.sectors.api.redis.enums.PubSubType;

public class PacketListener<T extends String> implements RedisListener<T> {

    private final PubSubType type;
    private final RTopic redisTopic;
    private final GsonBuilder gsonBilder = new GsonBuilder();
    private final Gson gson = gsonBilder.create();
    final PacketHandler packetHandler = new PacketHandlerImpl(SectorPlugin.getInstance());

    public PacketListener(PubSubType type, RTopic redisTopic) {
        this.type = type;
        this.redisTopic = redisTopic;
        this.redisTopic.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence charSequence, String packet) {
                final String[] split = packet.split("@");
                Class<? extends Packet> clzPacket = SectorPlugin.getInstance().getPacketManager().getPacketClass(Integer.parseInt(split[0]));
                if (clzPacket == null) return;
                final Packet p = gson.fromJson(split[1], clzPacket);
                p.handlePacket(packetHandler);
            }
        });
    }

    @Override
    public PubSubType getType() {
        return this.type;
    }

    @Override
    public RTopic getTopic() {
        return this.redisTopic;
    }

    @Override
    public void sendMessage(T message) {
        this.redisTopic.publish(message);
    }

}
