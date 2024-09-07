package pl.supereasy.sectors.api.redis.channels;

import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RTopic;
import pl.supereasy.sectors.SectorPlugin;

import java.util.UUID;

public class RedisChannel {

    public static RedisChannel INSTANCE = new RedisChannel();
    public RSet<String> onlineGlobalPlayers;
    public RTopic topTopic;
    public RTopic globalPacketTopic;
    public RTopic currentSectorTopic;
    public RTopic currentSectorUser;
    public RTopic apiTopic;
    public RMap<String, String> GUILDS;
    public RMap<UUID, String> USERS;
    public RMap<UUID, String> MEMBERS;
    public RMap<String, String> WARPS;
    public RMap<UUID, String> ALLIANCES;
    public RMap<UUID, String> WARS;
    public RMap<String, Long> ANTIGRIEF;
    public RMap<String, String> STONIARKI;
    public RMap<String, Boolean> CONDITIONS;
    public RSet<String> ERRORS;

    private RedisChannel() {
    }

    public void setupChannels(final SectorPlugin plugin) {
        this.onlineGlobalPlayers = plugin.getRedissonClient().getSet("ONLINE_PLAYERS");
        this.topTopic = plugin.getRedissonClient().getTopic("topTopic");
        this.globalPacketTopic = plugin.getRedissonClient().getTopic("globalPacketTopic");
        this.apiTopic = plugin.getRedissonClient().getTopic("API");
        GUILDS = plugin.getRedissonClient().getMap("GUILDS");
        USERS = plugin.getRedissonClient().getMap("USERS");
        MEMBERS = plugin.getRedissonClient().getMap("MEMBERS");
        WARPS = plugin.getRedissonClient().getMap("WARPS");
        ALLIANCES = plugin.getRedissonClient().getMap("ALLIANCES");
        WARS = plugin.getRedissonClient().getMap("WARS");
        CONDITIONS = plugin.getRedissonClient().getMap("CONDITIONS");
        ERRORS = plugin.getRedissonClient().getSet("ERRORS");
    }

    public void setupCurrentChannel(final String sectorName) {
        this.currentSectorTopic = SectorPlugin.getInstance().getRedissonClient().getTopic(sectorName);
        this.currentSectorUser = SectorPlugin.getInstance().getRedissonClient().getTopic(sectorName + "-user");
        ANTIGRIEF = SectorPlugin.getInstance().getRedissonClient().getMap("antiGriefBlocks-" + SectorPlugin.getInstance().getSectorManager().getCurrentSector().getSectorName());
        STONIARKI = SectorPlugin.getInstance().getRedissonClient().getMap("stoniarki-" + SectorPlugin.getInstance().getSectorManager().getCurrentSector().getSectorName());
    }

}
