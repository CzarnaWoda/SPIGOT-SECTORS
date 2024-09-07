package pl.supereasy.sectors.guilds.api;

import io.vavr.collection.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.enums.RelationType;
import pl.supereasy.sectors.guilds.impl.GuildMemberImpl;

import java.util.UUID;

public interface GuildManager<T extends Guild, M extends GuildMember>{

    void registerGuild(final Guild guild);

    Guild createGuild(final User user, final String guildTag, final String guildName, final Sector sector, final int locX, final int locZ);

    T getGuild(final String guildTag);

    T getGuild(final M guildMember);

    T getGuild(final UUID memberUUID);

    T getGuild(final Location location);

    void addMember(final Guild guild, final User user);

    void addMember(final Guild guild, final GuildMemberImpl guildMember);

    void removeMember(final Guild guild, final User user);

    void removeMember(final Guild guild, final GuildMember guildMember);

    boolean hasItems(final Player player);

    boolean canCreateGuild(final Location location);

    void deleteGuild(final Guild guild);

    Set<M> getGuildMembers(final T guild);

    RelationType getRelation(final User user, final User otherUser);

    RelationType getRelation(final Guild guild, final Guild otherGuild);

    Set<Guild> getGuilds();

    Set<Guild> currentSectorGuilds();


}
