package pl.supereasy.sectors.config;

import org.bukkit.plugin.Plugin;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.config.api.Configurable;

public class CurrentSectorConfig extends Configurable {

    private int sectorUniqueID;
    private int sectorNettyPort;
    private String mysql_server;
    private String mysql_user;
    private String mysql_password;
    private String mysql_database;
    private String redis_server;
    private String redis_password;
    private String apiHost;

    public CurrentSectorConfig() {
        super("currentsector.yml", "plugins/SectorPlugin/");

    }

    public int getSectorNettyPort() {
        return sectorNettyPort;
    }

    public int getSectorUniqueID() {
        return sectorUniqueID;
    }

    public String getMysql_database() {
        return mysql_database;
    }

    public String getMysql_password() {
        return mysql_password;
    }

    public String getMysql_server() {
        return mysql_server;
    }

    public String getMysql_user() {
        return mysql_user;
    }

    public String getApiHost() {
        return apiHost;
    }

    public String getRedis_password() {
        return redis_password;
    }

    public String getRedis_server() {
        return redis_server;
    }

    @Override
    public void loadConfig() {
        sectorUniqueID = getConfig().getInt("sector.uniqueid");
        sectorNettyPort = getConfig().getInt("sector.nettyport");
        mysql_server = getConfig().getString("mysql.server");
        mysql_user = getConfig().getString("mysql.user");
        mysql_password = getConfig().getString("mysql.password");
        mysql_database = getConfig().getString("mysql.database");
        apiHost = getConfig().getString("api.host");
        redis_server = getConfig().getString("redis.server");
        redis_password = getConfig().getString("redis.password");
    }
}
