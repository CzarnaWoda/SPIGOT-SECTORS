package pl.supereasy.sectors.guilds.wars.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.api.relations.RelationSolver;
import pl.supereasy.sectors.guilds.wars.api.War;
import pl.supereasy.sectors.guilds.wars.api.WarManager;

import java.util.HashSet;
import java.util.Set;

public class WarManagerImpl implements WarManager {

    private final SectorPlugin plugin;
    private final Set<War> wars = new HashSet<>();

    public WarManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public War createWar(Guild g, Guild g2) {
        War war = getWar(g, g2);
        if (war == null) {
            war = new WarImpl(g, g2);
            this.wars.add(war);
        }
        return war;
    }

    @Override
    public void registerWar(War war) {
        this.wars.add(war);
    }

    @Override
    public boolean removeAlliance(Guild g, Guild g2) {
        return false;
    }

    @Override
    public RelationSolver findWarBetween(Guild g, Guild g2) {
        return null;
    }


    @Override
    public War getWar(String g, String g2) {
        for (War war : this.wars) {
            if (war.getFirstGuild().getTag().equals(g) && war.getSecondGuild().getTag().equals(g2)) {
                return war;
            }
            if (war.getFirstGuild().getTag().equals(g2) && war.getSecondGuild().getTag().equals(g)) {
                return war;
            }
        }
        return null;
    }

    @Override
    public Set<War> getWars(String tag) {
        final Set<War> wars = new HashSet<>();
        for (War war : this.wars) {
            if (war.getFirstGuild().getTag().equals(tag) || war.getSecondGuild().getTag().equals(tag)) {
                wars.add(war);
            }
        }
        return wars;
    }

    @Override
    public boolean hasWar(String tag, String tag2) {
        for (War war : this.wars) {
            if (war.getFirstGuild().getTag().equals(tag) && war.getSecondGuild().getTag().equals(tag2)) {
                return true;
            }
            if (war.getFirstGuild().getTag().equals(tag2) && war.getSecondGuild().getTag().equals(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<War> getWars() {
        return this.wars;
    }
}
