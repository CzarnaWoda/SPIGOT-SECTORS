package pl.supereasy.sectors.api.redis.api;

import org.redisson.api.RTopic;
import pl.supereasy.sectors.api.redis.enums.PubSubType;

public interface RedisListener<T> {

    PubSubType getType();

    RTopic getTopic();

    void sendMessage(T message);

    default void sendMessage(final String sector, final T data) {

    }

}
