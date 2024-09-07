package pl.supereasy.sectors.guilds.api;

import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.api.Playerable;

import java.util.Set;

public interface GuildMember extends Playerable {

    void addPermission(final GuildPermission guildPermission);

    void removePermission(final GuildPermission guildPermission);

    default void changePermission(final GuildPermission guildPermission) {
        if (hasPermission(guildPermission)) {
            removePermission(guildPermission);
            return;
        }
        addPermission(guildPermission);
    }

    boolean hasPermission(final GuildPermission guildPermission);

    void removeAllPermissions();

    void addAllPermissions();

    Set<GuildPermission> getPermissions();

}
