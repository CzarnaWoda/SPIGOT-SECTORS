package pl.supereasy.sectors.core.user.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserJoinManager {

    private static final Cache<UUID,Long> afterJoinTimes = CacheBuilder.newBuilder().expireAfterWrite(3,TimeUnit.SECONDS).build();

    public static boolean hasJoinedNewly(UUID uuid) {
        return afterJoinTimes.getIfPresent(uuid) != null;
    }

    public static void registerJoinNewly(User user) {
        afterJoinTimes.put(user.getUUID(), System.currentTimeMillis());
    }
}
