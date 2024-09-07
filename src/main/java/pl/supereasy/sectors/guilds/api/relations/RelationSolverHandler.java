package pl.supereasy.sectors.guilds.api.relations;

import io.vavr.control.Option;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;

public interface RelationSolverHandler<T> {

    Option<T> findMarkedGuild();

    Option<T> findRelationGuildToMarked();

}
