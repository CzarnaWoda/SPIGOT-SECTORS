package pl.supereasy.sectors.api.redis.api;


import pl.supereasy.sectors.api.redis.enums.PubSubType;

import java.util.Map;

public interface RedisManager {

    RedisListener getPubSub(final PubSubType type);

    void registerListener(final RedisListener listener);

    Map<PubSubType, RedisListener> getChannels();
}
