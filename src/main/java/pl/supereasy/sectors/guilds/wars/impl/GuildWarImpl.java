package pl.supereasy.sectors.guilds.wars.impl;

import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.wars.api.GuildWar;

public class GuildWarImpl implements GuildWar {

    private String guildTag;
    private Guild guild;
    private int warKills;
    private int warDeaths;

    public GuildWarImpl() {
    }

    public GuildWarImpl(String guildTag, Guild guild) {
        this.guildTag = guildTag;
        this.guild = guild;
    }

    public GuildWarImpl(String guildTag, Guild guild, int warKills, int warDeaths) {
        this.guildTag = guildTag;
        this.guild = guild;
        this.warKills = warKills;
        this.warDeaths = warDeaths;
    }

    @Override
    public String getTag() {
        return this.guildTag;
    }

    @Override
    public Guild asGuild() {
        return this.guild;
    }

    @Override
    public int getKills() {
        return this.warKills;
    }

    @Override
    public int getDeaths() {
        return this.warDeaths;
    }

    @Override
    public void addKills(int kills) {
        this.warKills = this.warKills + kills;
    }

    @Override
    public void addDeaths(int deaths) {
        this.warDeaths = this.warDeaths + deaths;
    }

    @Override
    public void setKills(int kills) {
        this.warKills = kills;
    }

    @Override
    public void setDeaths(int deaths) {
        this.warDeaths = deaths;
    }
}
