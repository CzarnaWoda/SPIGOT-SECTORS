package pl.supereasy.sectors.api.redis.listeners;

import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.api.RedisListener;
import pl.supereasy.sectors.api.redis.enums.PubSubType;
import pl.supereasy.sectors.api.packets.impl.tops.TopPacket;

public class TopListener<T extends TopPacket> implements RedisListener<T> {

    private final PubSubType type;
    private final RTopic redisTopic;


    public TopListener(PubSubType type, RTopic redisTopic) {
        this.type = type;
        this.redisTopic = redisTopic;
        this.redisTopic.addListener(TopPacket.class, new MessageListener<TopPacket>() {
            @Override
            public void onMessage(CharSequence charSequence, TopPacket topPacket) {
                SectorPlugin.getInstance().getTopManager().replaceTop(topPacket.getTopType(), topPacket.getTopList());
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
    public void sendMessage(TopPacket message) {
        this.redisTopic.publish(message);
    }
}
