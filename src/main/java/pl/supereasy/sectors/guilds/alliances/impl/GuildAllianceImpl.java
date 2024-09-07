package pl.supereasy.sectors.guilds.alliances.impl;

import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GuildAllianceImpl implements GuildAlliance {

    private final String guildTag;
    private final Set<AlliancePermission> permissions;

    public GuildAllianceImpl(String guildTag) {
        this.guildTag = guildTag;
        this.permissions = new HashSet<>();
    }

    public GuildAllianceImpl(String guildTag, Set<AlliancePermission> permissions) {
        this.guildTag = guildTag;
        this.permissions = permissions;
    }

    @Override
    public Set<AlliancePermission> getPermissions() {
        return this.permissions;
    }

    @Override
    public String getTag() {
        return this.guildTag;
    }
}
