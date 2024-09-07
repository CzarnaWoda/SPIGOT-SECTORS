package pl.supereasy.sectors.guilds.alliances.impl;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.alliances.api.Alliance;
import pl.supereasy.sectors.guilds.alliances.api.AllianceManager;
import pl.supereasy.sectors.guilds.api.relations.RelationSolver;

import java.util.*;

public class AllianceManagerImpl implements AllianceManager {

    private final SectorPlugin plugin;
    private final Map<UUID, Alliance> alliances = new HashMap<>();

    public AllianceManagerImpl(SectorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void addAlliance(UUID uuid, Guild g, Guild g2) {
        if (!this.alliances.containsKey(uuid)) {
            this.alliances.put(uuid, new AllianceImpl(uuid, g, g2));
        }
    }

    @Override
    public void addAlliance(Guild g, Guild g2) {
        final Alliance alliance = getAlliance(g, g2);
        if (alliance == null) {
            final UUID uuid = UUID.randomUUID();
            this.alliances.put(uuid, new AllianceImpl(uuid, g, g2));
        }
    }

    @Override
    public void removeAlliance(Guild g, Guild g2) {
        final Alliance alliance = getAlliance(g, g2);
        if (alliance != null) {
            this.alliances.remove(alliance.getID());
        }
    }

    @Override
    public void registerAlliance(Alliance alliance) {
        this.alliances.put(alliance.getID(), alliance);
    }

    @Override
    public void deleteAlliance(Alliance alliance) {
        this.alliances.remove(alliance.getID());
    }

    @Override
    public RelationSolver findAllianceBetween(Guild g, Guild g2) {
        return new RelationSolver(g.getTag(), g2.getTag());//TODO 1 obiekt bez new
    }

    @Override
    public Set<Alliance> getAlliances(Guild guild) {
        final Set<Alliance> alliances = new HashSet<>();
        for (Alliance alliance : this.alliances.values()) {
            if (alliance.getFirstGuild().getTag().equals(guild.getTag()) || alliance.getSecondGuild().getTag().equals(guild.getTag())) {
                alliances.add(alliance);
            }
        }
        return alliances;
    }

    @Override
    public Set<String> getAlliancesTags(Guild guild) {
        final Set<String> alliances = new HashSet<>();
        for (Alliance alliance : this.alliances.values()) {
            if (alliance.getFirstGuild().getTag().equals(guild.getTag())) {
                alliances.add(alliance.getSecondGuild().getTag());
                continue;
            }
            if (alliance.getSecondGuild().getTag().equals(guild.getTag())) {
                alliances.add(alliance.getFirstGuild().getTag());
            }
        }
        return alliances;
    }

    //        this.plugin.getAllianceManager().findAllianceBetween(g1, g2).markFirstGuild().findAllianceGuildToMarked().onPresent -> .onEmpty


    @Override
    public Alliance getAlliance(Guild g, Guild g2) {
        for (Alliance alliance : this.alliances.values()) {
            if (alliance.getFirstGuild().getTag().equals(g.getTag()) && alliance.getSecondGuild().getTag().equals(g2.getTag())) {
                return alliance;
            }
            if (alliance.getFirstGuild().getTag().equals(g2.getTag()) && alliance.getSecondGuild().getTag().equals(g.getTag())) {
                return alliance;
            }
        }
        return null;
    }

    @Override
    public Alliance getAlliance(UUID id) {
        return this.alliances.get(id);
    }


    @Override
    public boolean hasAlliance(String tag, String tag2) {
        throw new RuntimeException("hasAlliance not implemented!");
    }

    @Override
    public boolean hasAlliance(Guild g, Guild g2) {
        return getAlliance(g, g2) != null;
    }

    @Override
    public Collection<Alliance> getAlliances() {
        return this.alliances.values();
    }
}
