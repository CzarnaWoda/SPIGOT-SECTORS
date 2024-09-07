package pl.supereasy.sectors.guilds.alliances.api;

import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.relations.RelationSolver;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface AllianceManager {


    void addAlliance(final UUID uuid, final Guild g, final Guild g2);

    void addAlliance(final Guild g, final Guild g2);

    void removeAlliance(final Guild g, final Guild g2);

    void registerAlliance(final Alliance alliance);

    void deleteAlliance(final Alliance alliance);

    RelationSolver findAllianceBetween(final Guild g, final Guild g2);

    Set<Alliance> getAlliances(final Guild guild);

    Set<String> getAlliancesTags(final Guild guild);

    Alliance getAlliance(Guild g, Guild g2);

    Alliance getAlliance(final UUID id);

    boolean hasAlliance(final String tag, final String tag2);

    default boolean hasAlliance(final Guild g, final Guild g2) {
        return hasAlliance(g.getTag(), g2.getTag());
    }

    Collection<Alliance> getAlliances();

}
