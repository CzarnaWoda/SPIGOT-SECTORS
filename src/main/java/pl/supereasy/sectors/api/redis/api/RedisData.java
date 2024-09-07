package pl.supereasy.sectors.api.redis.api;

import java.io.Serializable;

public interface RedisData extends Serializable {

    void insert(boolean async);

    void delete(boolean async);

}

