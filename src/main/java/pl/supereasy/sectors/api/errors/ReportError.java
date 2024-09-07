package pl.supereasy.sectors.api.errors;

import pl.supereasy.sectors.api.redis.channels.RedisChannel;

public class ReportError {

    public static void save(final String desc) {
        RedisChannel.INSTANCE.ERRORS.addAsync(desc);
    }

}
