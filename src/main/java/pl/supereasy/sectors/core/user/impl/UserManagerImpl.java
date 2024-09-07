package pl.supereasy.sectors.core.user.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.user.api.UserManager;

import java.util.*;

public class UserManagerImpl implements UserManager {

    private final SectorPlugin plugin;
    private final Map<UUID, User> users;
    private final Map<String, User> cacheUsers;

    public UserManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
        this.users = new HashMap<>();
        this.cacheUsers = new HashMap<>();
    }

    @Override
    public User getUser(UUID uuid) {
        return this.users.get(uuid);
    }

    @Override
    public User getUser(String userName) {
        final User u = this.cacheUsers.get(userName);
        if(u != null){
            return u;
        }
        for(final User user : this.users.values()){
            if(user.getName().equalsIgnoreCase(userName)){
                this.cacheUsers.put(userName, user);
                return user;
            }
        }
        return null;
    }

    @Override
    public void registerUser(User user) {
        this.users.put(user.getUUID(), user);
        this.cacheUsers.put(user.getName(), user);
    }

    @Override
    public io.vavr.collection.HashSet<User> getUsers() {
        return io.vavr.collection.HashSet.ofAll(this.users.values());
    }
}
