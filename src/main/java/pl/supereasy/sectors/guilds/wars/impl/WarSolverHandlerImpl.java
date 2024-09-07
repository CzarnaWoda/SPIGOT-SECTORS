package pl.supereasy.sectors.guilds.wars.impl;

import io.vavr.control.Option;
import pl.supereasy.sectors.guilds.api.relations.RelationSolverHandler;
import pl.supereasy.sectors.guilds.wars.api.GuildWar;
import pl.supereasy.sectors.guilds.wars.api.War;

public class WarSolverHandlerImpl implements RelationSolverHandler<GuildWar> {

    private final War war;
    private final String markedGuild;

    public WarSolverHandlerImpl(War war, String markedGuild) {
        this.war = war;
        this.markedGuild = markedGuild;
    }

    @Override
    public Option<GuildWar> findMarkedGuild() {
        if (this.war == null) {
            return null;
        }
        if (this.war.getFirstGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.war.getFirstGuild());
        }
        if (this.war.getSecondGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.war.getSecondGuild());
        }
        return null;
    }

    @Override
    public Option<GuildWar> findRelationGuildToMarked() {
        if (this.war == null) {
            return null;
        }
        if (this.war.getFirstGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.war.getSecondGuild());
        }
        if (this.war.getSecondGuild().getTag().equals(this.markedGuild)) {
            return Option.of(this.war.getFirstGuild());
        }
        return null;
    }
}
