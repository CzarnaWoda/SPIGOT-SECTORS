package pl.supereasy.sectors.api.packets.impl.guild;

import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.api.PacketHandler;

import java.util.UUID;

public class GuildCreatePacket extends Packet {

    private String guildName;
    private String guildTag;
    private String guildOwner;
    private UUID ownerUUID;
    private int guildLives;
    private String treasureItems;
    private Long createTime;
    private Long expireTime;
    private int centerX;
    private int centerZ;
    private int guildSize;
    private String sectorName;

    public GuildCreatePacket() {
    }

    public GuildCreatePacket(String guildName, String guildTag, String guildOwner, UUID ownerUUID, int guildLives, String treasureItems, Long createTime, Long expireTime, int centerX, int centerZ, int guildSize, String sectorName) {
        this.guildName = guildName;
        this.guildTag = guildTag;
        this.guildOwner = guildOwner;
        this.ownerUUID = ownerUUID;
        this.guildLives = guildLives;
        this.treasureItems = treasureItems;
        this.createTime = createTime;
        this.expireTime = expireTime;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.guildSize = guildSize;
        this.sectorName = sectorName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getTreasureItems() {
        return treasureItems;
    }

    public String getGuildOwner() {
        return guildOwner;
    }

    public String getGuildName() {
        return guildName;
    }

    public String getGuildTag() {
        return guildTag;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public int getGuildLives() {
        return guildLives;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public int getGuildSize() {
        return guildSize;
    }

    public String getSectorName() {
        return sectorName;
    }
}
