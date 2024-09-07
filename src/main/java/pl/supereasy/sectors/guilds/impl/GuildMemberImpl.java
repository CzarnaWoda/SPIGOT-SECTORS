package pl.supereasy.sectors.guilds.impl;

import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;

import java.util.*;
import java.util.stream.Stream;

public class GuildMemberImpl implements GuildMember {

    private final UUID memberUUID;
    private final String memberName;
    private final Set<GuildPermission> permissions;

    public GuildMemberImpl(final UUID memberUUID, final String memberName, final GuildPermission... permissions) {
        this.memberUUID = memberUUID;
        this.memberName = memberName;
        this.permissions = new HashSet<>();
        Stream.of(permissions).filter(Objects::nonNull).forEach(this::addPermission);
    }

    @Override
    public void addPermission(GuildPermission guildPermission) {
        this.permissions.add(guildPermission);
    }

    @Override
    public void removePermission(GuildPermission guildPermission) {
        this.permissions.remove(guildPermission);
    }

    @Override
    public boolean hasPermission(GuildPermission guildPermission) {

        return this.permissions.contains(guildPermission);
    }

    @Override
    public void addAllPermissions() {
        this.permissions.addAll(Arrays.asList(GuildPermission.values()));
    }

    @Override
    public void removeAllPermissions() {
        this.permissions.clear();
    }

    @Override
    public Set<GuildPermission> getPermissions() {
        return this.permissions;
    }

    @Override
    public UUID getUUID() {
        return this.memberUUID;
    }

    @Override
    public String getName() {
        return this.memberName;
    }
}
