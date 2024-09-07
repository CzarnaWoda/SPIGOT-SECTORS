package pl.supereasy.sectors.guilds.wars.api;

import pl.supereasy.sectors.guilds.Guild;

public interface GuildWar {

    String getTag();

    Guild asGuild();

    int getKills();

    int getDeaths();

    void addKills(int kills);

    void addDeaths(int deaths);

    void setKills(int kills);

    void setDeaths(int deaths);

}
