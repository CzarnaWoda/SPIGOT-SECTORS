package pl.supereasy.sectors.guilds;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildDestroyPacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildMessagePacket;
import pl.supereasy.sectors.api.packets.impl.guild.GuildTakeLivePacket;
import pl.supereasy.sectors.api.redis.api.RedisData;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.regions.api.Region;
import pl.supereasy.sectors.api.regions.impl.RegionBuilder;
import pl.supereasy.sectors.api.regions.impl.RegionImpl;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.alliances.impl.AllianceImpl;
import pl.supereasy.sectors.guilds.api.GuildMember;
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.impl.GuildMemberImpl;
import pl.supereasy.sectors.guilds.log.impl.GuildLogActionImpl;
import pl.supereasy.sectors.guilds.regeneration.impl.RegenerationBlock;
import pl.supereasy.sectors.guilds.treasury.GuildTreasuryImpl;
import pl.supereasy.sectors.util.*;
import pl.supereasy.sectors.util.api.IdentifableName;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Guild implements RedisData, IdentifableName {


    private final String guildName;
    private final String guildTag;
    private String ownerName;
    private UUID ownerUUID;
    private int guildLives;
    private int heartDurability;
    private final long guildCreateTime;
    private long guildExpireTime;
    private final String worldName;
    private final int centerX;
    private final int centerZ;
    private final int guildSize;
    private long guildLastExplosion;
    private long guildLastTakeLive;
    private long guildLastBreakHeart;
    private boolean friendlyFire;
    private boolean allianceFriendlyFire;
    private final Sector guildSector;
    private Location homeLocation;
    private final RegionImpl guildRegion;
    private final List<GuildPermission> defaultPermissions;
    private final Map<UUID, GuildMemberImpl> guildMembers;
    private transient final Cache<UUID, String> invites;
    private transient final Cache<String, Long> allianceInvites;
    //private transient final io.vavr.collection.Set<>
    private final GuildTreasuryImpl guildTreasury;
    private final LinkedList<GuildLogActionImpl> guildLogActions = new LinkedList<>();
    private final Set<RegenerationBlock> regenerationBlocks = new HashSet<>();
    private Material collectMaterial;
    private int collectAmount;
    private int collectedAmount;
    private final ConcurrentHashMap<String, String> permissionPresets;
    private int winWars;
    private int loseWars;
    private int drawWars;


    public Guild() {
        this.guildName = null;
        this.guildTag = null;
        this.ownerUUID = null;
        this.guildSector = null;
        this.guildLives = 3;
        this.guildCreateTime = -1L;
        this.guildExpireTime = -1L;
        this.worldName = "world";
        this.centerX = 0;
        this.centerZ = 0;
        this.guildSize = 0; //guild region @up
        this.guildLastExplosion = -1L;
        this.guildLastTakeLive = System.currentTimeMillis();
        this.friendlyFire = false;
        this.allianceFriendlyFire = false;
        this.guildMembers = new HashMap<>();
        this.guildRegion = RegionBuilder.builder()
                .withRegionSize(this.guildSize)
                .withCenterX(this.centerX)
                .withCenterZ(this.centerZ)
                .build();
        this.invites = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
        this.allianceInvites = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
        this.defaultPermissions = Arrays.asList(GuildPermission.BREAK, GuildPermission.PLACE);
        this.guildTreasury = new GuildTreasuryImpl();
        this.collectMaterial = Material.AIR;
        this.collectAmount = 0;
        this.collectedAmount = 0;
        this.permissionPresets = new ConcurrentHashMap<>();
        permissionPresets.put("SZABLON-1","BREAK;PLACE");
        permissionPresets.put("SZABLON-2","BREAK;PLACE");
        permissionPresets.put("SZABLON-3","BREAK;PLACE");
        permissionPresets.put("SZABLON-4","BREAK;PLACE");
        permissionPresets.put("SZABLON-5","BREAK;PLACE");
        permissionPresets.put("SZABLON-6","BREAK;PLACE");
        permissionPresets.put("SZABLON-7","BREAK;PLACE");
        permissionPresets.put("SZABLON-8","BREAK;PLACE");
    }

    public Guild(String guildName, String guildTag, String ownerName, UUID ownerUUID, int centerX, int centerZ, final Sector sector) {
        this.guildName = guildName;
        this.guildTag = guildTag;
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.guildLives = 3;
        this.guildCreateTime = System.currentTimeMillis();
        this.guildExpireTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(3);
        this.guildSize = 50;
        this.worldName = "world";
        this.guildLastExplosion = -1L;
        this.guildLastTakeLive = System.currentTimeMillis();
        this.friendlyFire = false;
        this.allianceFriendlyFire = false;
        this.guildMembers = new HashMap<>();
        this.guildSector = sector;
        this.guildRegion = RegionBuilder.builder()
                .withRegionSize(this.guildSize)
                .withCenterX(this.centerX)
                .withCenterZ(this.centerZ)
                .build();
        this.homeLocation = this.guildRegion.getCenter();
        this.invites = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
        this.allianceInvites = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
        this.defaultPermissions = Arrays.asList(GuildPermission.BREAK, GuildPermission.PLACE);
        this.guildTreasury = new GuildTreasuryImpl();
        this.collectMaterial = Material.AIR;
        this.collectAmount = 0;
        this.collectedAmount = 0;
        this.permissionPresets = new ConcurrentHashMap<>();
        permissionPresets.put("SZABLON-1","BREAK;PLACE");
        permissionPresets.put("SZABLON-2","BREAK;PLACE");
        permissionPresets.put("SZABLON-3","BREAK;PLACE");
        permissionPresets.put("SZABLON-4","BREAK;PLACE");
        permissionPresets.put("SZABLON-5","BREAK;PLACE");
        permissionPresets.put("SZABLON-6","BREAK;PLACE");
        permissionPresets.put("SZABLON-7","BREAK;PLACE");
        permissionPresets.put("SZABLON-8","BREAK;PLACE");
    }

    public Guild(String guildName, String guildTag, String ownerName, UUID ownerUUID, int centerX, int centerZ, final Sector sector, int guildLives, long guildCreateTime, long guildExpireTime, int guildSize,
                 long guildLastExplosion,
                 long guildLastTakeLive,
                 boolean friendlyFire,
                 boolean allianceFriendlyFire,
                 int treasuryCoins,
                 Inventory treasuryInventory
    ) {
        this.guildName = guildName;
        this.guildTag = guildTag;
        this.ownerUUID = ownerUUID;
        this.ownerName = ownerName;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.guildLives = guildLives;
        this.guildCreateTime = guildCreateTime;
        this.guildExpireTime = guildExpireTime;
        this.guildSize = guildSize;
        this.worldName = "world";
        this.guildLastExplosion = guildLastExplosion;
        this.guildLastTakeLive = guildLastTakeLive;
        this.friendlyFire = friendlyFire;
        this.allianceFriendlyFire = allianceFriendlyFire;
        this.guildMembers = new HashMap<>();
        this.guildSector = sector;
        this.guildRegion = RegionBuilder.builder()
                .withRegionSize(this.guildSize)
                .withCenterX(this.centerX)
                .withCenterZ(this.centerZ)
                .build();
        this.homeLocation = this.guildRegion.getCenter();
        this.invites = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
        this.allianceInvites = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();
        this.defaultPermissions = Arrays.asList(GuildPermission.BREAK, GuildPermission.PLACE);
        this.guildTreasury = new GuildTreasuryImpl(treasuryCoins);
        this.collectMaterial = Material.AIR;
        this.collectAmount = 0;
        this.collectedAmount = 0;
        this.permissionPresets = new ConcurrentHashMap<>();
        permissionPresets.put("SZABLON-1","BREAK;PLACE");
        permissionPresets.put("SZABLON-2","BREAK;PLACE");
        permissionPresets.put("SZABLON-3","BREAK;PLACE");
        permissionPresets.put("SZABLON-4","BREAK;PLACE");
        permissionPresets.put("SZABLON-5","BREAK;PLACE");
        permissionPresets.put("SZABLON-6","BREAK;PLACE");
        permissionPresets.put("SZABLON-7","BREAK;PLACE");
        permissionPresets.put("SZABLON-8","BREAK;PLACE");
    }

    public boolean isCollectEnable(){
        return getCollectMaterial() != Material.AIR && getCollectAmount() != 0;
    }
    public void addCollectAmount(int amount){
        this.collectAmount += amount;
    }

    public void setCollectAmount(int collectAmount) {
        this.collectAmount = collectAmount;
    }

    public void setCollectedAmount(int collectedAmount) {
        this.collectedAmount = collectedAmount;
    }


    public String getTag() {
        return this.guildTag;
    }

    public String getGuildName() {
        return guildName;
    }

    public Location getHomeLocation() {
        return homeLocation;
    }

    public Sector getGuildSector() {
        return guildSector;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    public Map<UUID, GuildMemberImpl> getMembers() {
        return this.guildMembers;
    }

    public Region getGuildRegion() {
        return guildRegion;
    }

    public boolean isOwner(final Player p){
        return isOwner(p.getUniqueId());
    }

    public boolean isOwner(final UUID uuid){
        return this.ownerUUID.equals(uuid);
    }

    public boolean isMember(final UUID uuid){
        return this.guildMembers.containsKey(uuid);
    }

    public GuildMember getMember(final UUID uuid){
        return this.guildMembers.get(uuid);
    }

    public void addMember(final UUID uuid, final GuildMemberImpl guildMember) {
        this.guildMembers.put(uuid, guildMember);
    }

    public void removeMember(final UUID uuid) {
        this.guildMembers.remove(uuid);
    }

    public int getCollectAmount() {
        return collectAmount;
    }

    public int getCollectedAmount() {
        return collectedAmount;
    }

    public Material getCollectMaterial() {
        return collectMaterial;
    }
    public void setCollectMaterial(Material material){
        this.collectMaterial = material;
    }
    public Cache<UUID, String> getInvites() {
        return invites;
    }

    public boolean hasInvite(final UUID uuid) {
        return this.invites.getIfPresent(uuid) != null;
    }

    public Cache<String, Long> getAllianceInvites() {
        return allianceInvites;

    }
    public List<GuildPermission> getPreset(String name){
        String preset = getPermissionPresets().get(name);
        List<GuildPermission> permissions = new ArrayList<>();
        if(preset != null){
            String[] array = preset.split(";");
            for(String s : array){
                if(!s.equals("")) {
                    permissions.add(GuildPermission.valueOf(s));
                }
            }
        }
        return permissions;
    }
    public void renamePreset(String previousName, String newName){
        String s = getPermissionPresets().get(previousName);
        getPermissionPresets().remove(previousName);
        getPermissionPresets().put(newName,s);
    }
    public void addToPreset(GuildPermission permission, String presetName){
        List<GuildPermission> permissions = getPreset(presetName);

        permissions.add(permission);
        String preset = StringUtils.join(permissions,";");

        getPermissionPresets().put(presetName,preset);
    }
    public void removeFromPreset(GuildPermission permission, String presetName){
        List<GuildPermission> permissions = getPreset(presetName);

        permissions.remove(permission);
        String preset = StringUtils.join(permissions,";");

        getPermissionPresets().put(presetName,preset);
    }
    public boolean hasAllianceInvite(final String guildTag) {
        return this.allianceInvites.getIfPresent(guildTag) != null;
    }

    public ConcurrentHashMap<String, String> getPermissionPresets() {
        return permissionPresets;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }



    public GuildTreasuryImpl getGuildTreasury() {
        return guildTreasury;
    }

    public boolean isDuringExplosionAttack() {
        return this.guildLastExplosion + Time.SECOND.getTime(30) > System.currentTimeMillis();
    }

    public Set<GuildMember> getOnlineMembers() {
        final Set<GuildMember> members = new HashSet<>();
        for (GuildMember guildMember : this.guildMembers.values()) {
            final User user = SectorPlugin.getInstance().getUserManager().getUser(guildMember.getUUID());
            if(user != null && user.isOnline()){
                members.add(guildMember);
            }
        }
        return members;
    }

    public Set<Player> getOnlineMembersAsPlayers() {
        final Set<Player> members = new HashSet<>();
        for (UUID uuid : this.guildMembers.keySet()) {
            final Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                members.add(p);
            }
        }
        return members;
    }


    public void sendGuildMessage(final String msg, boolean betweenSectors) {
        if (betweenSectors) {
            final Packet packet = new GuildMessagePacket(this.guildTag, msg);
            SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
        } else {
            sendGuildMessage(msg);
        }
    }

    public void sendGuildMessage(final String msg) {
        for (Player p : getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(p, msg);
        }
    }

    public void sendAllianceMessage(final String msg, boolean betweenSectors) {
        if (betweenSectors) {
            final Packet packet = new GuildMessagePacket(this.guildTag, msg);
            SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
        } else {
            sendAllianceMessage(msg);
        }
    }

    public void sendAllianceMessage(final String msg) {
       /* for (Guild g : getGuildAlliancesAsGuild()) {
            for (Player p : g.getOnlineMembersAsPlayers()) {
                ChatUtil.sendMessage(p, msg);
            }
        }
        */
    }


    public void broadcastGuildMessage(final String msg) {
        for (Player p : getOnlineMembersAsPlayers()) {
            ChatUtil.sendMessage(p, " &6&lGILDIE &8» &7" + msg);
        }
    }

    public long getGuildLastExplosion() {
        return guildLastExplosion;
    }


    public void setGuildLastExplosion(long guildLastExplosion) {
        this.guildLastExplosion = guildLastExplosion;
    }

    public long getGuildLastTakeLive() {
        return guildLastTakeLive;
    }

    public void setGuildLastTakeLive(long guildLastTakeLive) {
        this.guildLastTakeLive = guildLastTakeLive;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getGuildLives() {
        return guildLives;
    }

    public void removeGuildLive(final int i) {
        this.guildLives -= i;
        this.setGuildLastTakeLive(System.currentTimeMillis());
    }

    public long getGuildCreateTime() {
        return guildCreateTime;
    }

    public long getGuildExpireTime() {
        return guildExpireTime;
    }

    public void setGuildExpireTime(long guildExpireTime) {
        this.guildExpireTime = guildExpireTime;
    }

    public String getWorldName() {
        return worldName;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public int getGuildSize() {
        return guildSize;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public void setAllianceFriendlyFire(boolean allianceFriendlyFire) {
        this.allianceFriendlyFire = allianceFriendlyFire;
    }
    

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public boolean isAllianceFriendlyFire() {
        return allianceFriendlyFire;
    }

    public List<GuildPermission> getDefaultPermissions() {
        return defaultPermissions;
    }

    public Map<UUID, GuildMemberImpl> getGuildMembers() {
        return guildMembers;
    }

    public List<GuildLogActionImpl> getLogActions() {
        return guildLogActions;
    }

    public void registerLogAction(final GuildLogActionImpl logAction) {
        this.guildLogActions.add(logAction);
    }

    public Set<RegenerationBlock> getRegenerationBlocks() {
        return regenerationBlocks;
    }

    public void addCollectedAmount(int amonut){
        this.collectedAmount +=  amonut;
    }

    public int getPoints() {
        int points = 0;
        for (GuildMember memebers : getGuildMembers().values()) {
            final User user = SectorPlugin.getInstance().getUserManager().getUser(memebers.getUUID());
            points += user.getPoints();
        }
        points = points / getGuildMembers().size();
        points = (int) (points + (10*getKDr()));
        return points;
    }
    public int getKills(){
        int kills = 0;
        for(GuildMember members : getGuildMembers().values()){
            final User user = SectorPlugin.getInstance().getUserManager().getUser(members.getUUID());
            kills += user.getKills();
        }
        return kills;
    }
    public int getDeaths(){
        int deaths = 0;
        for(GuildMember members : getGuildMembers().values()){
            final User user = SectorPlugin.getInstance().getUserManager().getUser(members.getUUID());
            deaths += user.getDeaths();
        }
        return deaths;
    }
    public int getAssist(){
        int assists = 0;
        for(GuildMember members : getGuildMembers().values()){
            final User user = SectorPlugin.getInstance().getUserManager().getUser(members.getUUID());
            assists += user.getAssists();
        }
        return assists;
    }
    public double getKDr(){
        if(getKills() == 0 && getDeaths() == 0){
            return 0.0D;
        }
        if(getKills() > 0 && getDeaths() == 0){
            return getKills();
        }
        if(getDeaths() > 0 && getKills() == 0){
            return -getDeaths();
        }
        return MathUtil.round(getKills()/getDeaths(), 2);
    }
    public void sendInfoMessage(Player player){
        ChatUtil.sendMessage(player,"&8->> [ &a&l" + getTag() + "&8 ] &2" + getGuildName() + "&8 <<-");
        ChatUtil.sendMessage(player, "&8->> &7Zalozyciel&8: &6" + getOwnerName());
        ChatUtil.sendMessage(player, "&8->> &7Punkty&8: &6" + getPoints());
        ChatUtil.sendMessage(player, "&8->> &7Statystyki PvP &8(&a&nKILLS/DEATHS/KDR&8): &6" + getKills() + "&8/&6" + getDeaths() + "&8/&6" + getKDr());
        ChatUtil.sendMessage(player, "&8->> &7Zycia&8: &4" + ChatUtil.getHearts(getGuildLives()));
        ChatUtil.sendMessage(player, "&8->> &7Rozmiar cuboida&8: &6" + getGuildRegion().getSize() + "&8x&6" + getGuildRegion().getSize());
        ChatUtil.sendMessage(player, "&8->> &7Ochrona TnT&8: &6" + (getGuildCreateTime() + Time.HOUR.getTime(24) > System.currentTimeMillis() ? "&a%V%&8, &2" + Util.secondsToString((int) (((getGuildCreateTime() + Time.HOUR.getTime(24)) - System.currentTimeMillis()) / 1000L)) : "&c%X%"));
        ChatUtil.sendMessage(player, "&8->> &7Atak&8: &6" + (getGuildLastTakeLive() + Time.HOUR.getTime(24) < System.currentTimeMillis() ? "&a%V%" : "&c%X%, &4" + Util.secondsToString((int) (((getGuildLastTakeLive() + Time.HOUR.getTime(24)) - System.currentTimeMillis()) / 1000L))));
        ChatUtil.sendMessage(player, "&8->> &7Utworzona&8: &6" + TimeUtil.getDate(getGuildCreateTime()));
        ChatUtil.sendMessage(player, "&8->> &7Wygasa za&8: &6" + Util.secondsToString((int) ((getGuildExpireTime() - System.currentTimeMillis()) / 1000L)));
        ChatUtil.sendMessage(player, "&8->> &7Czlonkowie&8: &6" + getMembersToString());
        //ChatUtil.sendMessage(player, "&8->> &7Sojusze&8: &6" + StringUtils.join(getGuildAlliances().keySet(), "&7, &6"));
        ChatUtil.sendMessage(player, "&8->> [ &a&l" + getTag() + "&8 ] &2" + getGuildName() + "&8 <<-");
    }

    private String getMembersToString() {
        String[] array = new String[getGuildMembers().size()];
        int i = 0;
        for (GuildMember member : getGuildMembers().values()) {
            final User user = SectorPlugin.getInstance().getUserManager().getUser(member.getUUID());
            array[i] = (user.isOnline() ? ChatColor.GREEN : ChatColor.DARK_RED) + user.getName();
            i++;
        }
        return StringUtils.join(array, "&7, ");
    }

    public void setHeartDurability(int heartDurability) {
        this.heartDurability = heartDurability;
    }

    public void setGuildLastBreakHeart(long guildLastBreakHeart) {
        this.guildLastBreakHeart = guildLastBreakHeart;
    }

    public int getHeartDurability() {
        return heartDurability;
    }

    public long getGuildLastBreakHeart() {
        return guildLastBreakHeart;
    }

    public void regenerateHeartPoints(final int val) {
        this.heartDurability = this.heartDurability + val;
    }


    public void addWinWars(int wins) {
        winWars += wins;
    }

    public void addLoseWars(int lose) {
        loseWars += lose;
    }

    public void addDrawWars(int draw) {
        drawWars += draw;
    }

    public int getWinWars() {
        return winWars;
    }

    public void setWinWars(int winWars) {
        this.winWars = winWars;
    }

    public int getLoseWars() {
        return loseWars;
    }

    public void setLoseWars(int loseWars) {
        this.loseWars = loseWars;
    }

    public int getDrawWars() {
        return drawWars;
    }

    public void setDrawWars(int drawWars) {
        this.drawWars = drawWars;
    }

    @Override
    public void insert(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.GUILDS.putAsync(this.guildTag, GsonUtil.toJson(this));
        } else {
            RedisChannel.INSTANCE.GUILDS.put(this.guildTag, GsonUtil.toJson(this));
        }
    }

    @Override
    public void delete(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.GUILDS.removeAsync(this.guildTag);
        } else {
            RedisChannel.INSTANCE.GUILDS.remove(this.guildTag);
        }
    }

    @Override
    public String getIdentifableName() {
        return this.guildTag.toUpperCase();
    }
}
