package pl.supereasy.sectors.guilds.builders;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.guilds.Guild;

import java.util.UUID;

public final class GuildBuilder {
    private String guildName;
    private String guildTag;
    private String ownerName;
    private UUID ownerUUID;
    private int guildLives;
    private long guildCreateTime;
    private long guildExpireTime;
    private int centerX;
    private int centerZ;
    private int guildSize;
    private long guildLastExplosion;
    private long guildLastTakeLive;
    private boolean friendlyFire;
    private boolean allianceFriendlyFire;
    private Sector sector;
    private int treasuryCoins;
    private Inventory treasuryInventory;

    private GuildBuilder() {
    }

    public static GuildBuilder aGuild() {
        return new GuildBuilder();
    }


    public GuildBuilder withGuildName(String guildName) {
        this.guildName = guildName;
        return this;
    }

    public GuildBuilder withGuildTag(String guildTag) {
        this.guildTag = guildTag;
        return this;
    }

    public GuildBuilder withOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public GuildBuilder withOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
        return this;
    }

    public GuildBuilder withGuildLives(int guildLives) {
        this.guildLives = guildLives;
        return this;
    }

    public GuildBuilder withGuildCreateTime(long guildCreateTime) {
        this.guildCreateTime = guildCreateTime;
        return this;
    }

    public GuildBuilder withGuildExpireTime(long guildExpireTime) {
        this.guildExpireTime = guildExpireTime;
        return this;
    }

    public GuildBuilder withCenterX(int centerX) {
        this.centerX = centerX;
        return this;
    }

    public GuildBuilder withCenterZ(int centerZ) {
        this.centerZ = centerZ;
        return this;
    }

    public GuildBuilder withGuildSize(int guildSize) {
        this.guildSize = guildSize;
        return this;
    }

    public GuildBuilder withGuildLastExplosion(long guildLastExplosion) {
        this.guildLastExplosion = guildLastExplosion;
        return this;
    }

    public GuildBuilder withGuildLastTakeLive(long guildLastTakeLive) {
        this.guildLastTakeLive = guildLastTakeLive;
        return this;
    }

    public GuildBuilder withFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        return this;
    }

    public GuildBuilder withAllianceFriendlyFire(boolean allianceFriendlyFire) {
        this.allianceFriendlyFire = allianceFriendlyFire;
        return this;
    }

    public GuildBuilder withSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public GuildBuilder withTreasuryCoins(int coins) {
        this.treasuryCoins = coins;
        return this;
    }

    public GuildBuilder withTreasuryInventory(Inventory inventory) {
        this.treasuryInventory = inventory;
        return this;
    }

    public Guild build() {
        return new Guild(guildName, guildTag, ownerName, ownerUUID, centerX, centerZ, sector, guildLives, guildCreateTime, guildExpireTime, guildSize, guildLastExplosion, guildLastTakeLive, friendlyFire, allianceFriendlyFire, treasuryCoins, treasuryInventory);
    }
}
