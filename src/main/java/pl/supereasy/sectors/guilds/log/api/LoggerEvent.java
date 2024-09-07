package pl.supereasy.sectors.guilds.log.api;

import pl.supereasy.sectors.core.user.impl.User;

public interface LoggerEvent<T> {

    void update(final User user, T type);

}
