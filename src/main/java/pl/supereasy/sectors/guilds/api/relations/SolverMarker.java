package pl.supereasy.sectors.guilds.api.relations;

import pl.supereasy.sectors.guilds.alliances.api.Alliance;

public interface SolverMarker {

    RelationSolverHandler markFirstGuild();

    RelationSolverHandler markSecondGuild();

    Alliance get();

}
