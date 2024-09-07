package pl.supereasy.sectors.guilds.alliances.api;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.enums.AlliancePermission;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.util.api.IdentifableName;
import pl.supereasy.sectors.util.api.Tagable;

import java.util.Set;

public interface GuildAlliance extends Tagable {

    Set<AlliancePermission> getPermissions();

    default Guild asGuild() {
        return SectorPlugin.getInstance().getGuildManager().getGuild(getTag());
    }

    default boolean hasPermission(final AlliancePermission permission) {
        return getPermissions().contains(permission);
    }

    default boolean addPermission(final AlliancePermission permission) {
        return getPermissions().add(permission);
    }

    default boolean removePermission(final AlliancePermission permission) {
        return getPermissions().remove(permission);
    }

}
