package pl.supereasy.sectors.guilds.alliances.impl;

import io.vavr.control.Option;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.api.GuildAlliance;
import pl.supereasy.sectors.guilds.api.relations.RelationSolverHandler;

public class AllianceSolverHandlerImpl implements RelationSolverHandler<GuildAlliance> {

    private final Alliance alliance;
    private final String markedGuild;

    public AllianceSolverHandlerImpl(Alliance alliance, String markedGuild) {
        this.alliance = alliance;
        this.markedGuild = markedGuild;
    }

    @Override
    public Option<GuildAlliance> findMarkedGuild() {
        if (this.alliance == null) {
            return null;
        }
        if (this.alliance.getFirstGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.alliance.getFirstGuild());
        }
        if (this.alliance.getSecondGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.alliance.getSecondGuild());
        }
        return null;
    }

    @Override
    public Option<GuildAlliance> findRelationGuildToMarked() {
        if (this.alliance == null) {
            return null;
        }
        if (this.alliance.getFirstGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.alliance.getSecondGuild());
        }
        if (this.alliance.getSecondGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.alliance.getFirstGuild());
        }
        return null;
    }
}
