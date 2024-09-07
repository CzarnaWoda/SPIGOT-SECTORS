package pl.supereasy.sectors.guilds.wars.api;

import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.relations.RelationSolver;

import java.util.Set;

public interface WarManager {

    War createWar(final Guild g, final Guild g2);

    void registerWar(final War war);

    boolean removeAlliance(final Guild g, final Guild g2);

    RelationSolver findWarBetween(final Guild g, final Guild g2);

    default War getWar(final Guild g, final Guild g2) {
        return getWar(g.getTag(), g2.getTag());
    }

    War getWar(final String g, final String g2);

    default Set<War> getWars(final Guild g) {
        return getWars(g.getTag());
    }

    Set<War> getWars(final String tag);

    boolean hasWar(final String tag, final String tag2);

    default boolean hasWar(final Guild g, final Guild g2) {
        return hasWar(g.getTag(), g2.getTag());
    }

    Set<War> getWars();

}
