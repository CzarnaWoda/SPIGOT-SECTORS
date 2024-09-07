package pl.supereasy.sectors.guilds.api.relations;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.impl.AllianceSolverHandlerImpl;

public class RelationSolver implements SolverMarker {

    private final String guild1;
    private final String guild2;
    private Alliance alliance;

    public RelationSolver(String guild1, String guild2) {
        this.guild1 = guild1;
        this.guild2 = guild2;
        for (Alliance alliance : SectorPlugin.getInstance().getAllianceManager().getAlliances()) {
            if (alliance.getFirstGuild().getTag().equals(guild1) && alliance.getSecondGuild().getTag().equals(guild2)) {
                this.alliance = alliance;
                break;
            }
            if (alliance.getFirstGuild().getTag().equals(guild2) && alliance.getSecondGuild().getTag().equals(guild1)) {
                this.alliance = alliance;
                break;
            }
        }
    }

    @Override
    public RelationSolverHandler markFirstGuild() {
        return new AllianceSolverHandlerImpl(this.alliance, this.guild1);
    }

    @Override
    public RelationSolverHandler markSecondGuild() {
        return new AllianceSolverHandlerImpl(this.alliance, this.guild2);
    }

    @Override
    public Alliance get() {
        return this.alliance;
    }
}
