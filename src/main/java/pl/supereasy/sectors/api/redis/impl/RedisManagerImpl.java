package pl.supereasy.sectors.api.redis.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.redis.api.RedisListener;
import pl.supereasy.sectors.api.redis.api.RedisManager;
import pl.supereasy.sectors.api.redis.enums.PubSubType;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RedisManagerImpl implements RedisManager {

    private final SectorPlugin plugin;
    private final Map<PubSubType, RedisListener> topics;

    public RedisManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.topics = new HashMap<>();
    }

    @Override
    public RedisListener getPubSub(PubSubType type) {
        return this.topics.get(type);
    }

    @Override
    public void registerListener(RedisListener listener) {
        this.topics.put(listener.getType(), listener);
        plugin.getLogger().log(Level.INFO, "Registered redis listener for " + listener.getType().name());
    }

    @Override
    public Map<PubSubType, RedisListener> getChannels() {
        return this.topics;
    }
}
