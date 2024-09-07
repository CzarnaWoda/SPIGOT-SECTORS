package pl.supereasy.sectors.core.user.api;

import io.vavr.collection.HashSet;
import pl.supereasy.sectors.core.user.impl.User;

import java.util.UUID;

public interface UserManager {

    User getUser(final UUID uuid);

    User getUser(final String userName);

    void registerUser(final User user);

    HashSet<User> getUsers();

}
