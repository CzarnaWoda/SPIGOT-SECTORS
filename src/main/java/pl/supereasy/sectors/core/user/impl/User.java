package pl.supereasy.sectors.core.user.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.chat.MessageToUserPacket;
import pl.supereasy.sectors.api.redis.api.RedisData;
import pl.supereasy.sectors.api.redis.channels.RedisChannel;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.drop.data.Drop;
import pl.supereasy.sectors.core.home.api.Home;
import pl.supereasy.sectors.core.home.impl.HomeImpl;
import pl.supereasy.sectors.core.incognito.impl.Incognito;
import pl.supereasy.sectors.core.user.api.UserChat;
import pl.supereasy.sectors.core.user.enderchests.impl.EnderChestImpl;
import pl.supereasy.sectors.core.user.enums.EnderType;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.enums.UserPermission;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class User implements RedisData {

    private UUID uuid;
    private String userName;
    private ItemStack[] inventory;
    private ItemStack[] armorInventory;
    private final UserChatImpl userChat = new UserChatImpl();
    private UserGroup userGroup = UserGroup.GRACZ;
    private int points = 500;
    private int kills = 0;
    private int assists = 0;
    private int deaths = 0;
    private int logouts = 0;
    private long timePlay = 0L;
    private int eatKox = 0;
    private int eatRef = 0;
    private int throwPearl = 0;
    private int openedCase = 0;
    private int minedStone = 0;
    private int spendMoney = 0;
    private int playerExp = 0;
    private double coins = 0;
    private int level = 0;
    private double exp = 0.0;
    private final HashSet<String> disableDrops = new HashSet<>();
    private transient Guild guild;
    private Sector sector;
    private final transient Cache<String, Long> tpaRequests = CacheBuilder.newBuilder().expireAfterWrite(25, TimeUnit.SECONDS).build();
    private final Map<String, HomeImpl> homes = new HashMap<>();
    private final Set<UUID> ignoredUsers = new HashSet<>();
    private final Set<UserPermission> userPermissions = new HashSet<>();
    private int depositKox = 0;
    private int depositRef = 0;
    private int depositPearl = 0;
    private int depositEmeraldBlocks = 0;
    private int depositFishingRod = 0;
    private int depositThrowTNT = 0;
    private int depositSnowBalls = 0;
    private long protection = System.currentTimeMillis() + Time.MINUTE.getTime(5);
    private String lastKill = null;
    private long lastKillTime = System.currentTimeMillis();
    private long turboDropTime = 0L;
    private long turboExpTime = 0L;
    private final List<String> achievements = new ArrayList<>();
    private List<String> quickSell = new ArrayList<>();
    private List<Long> kitTimes = Arrays.asList(0L, 0L, 0L, 0L, 0L);
    private Long lastUpdate = System.currentTimeMillis();
    private final Map<EnderType, EnderChestImpl> enderChests = new HashMap<>();
    //private final Incognito incognito = new Incognito();
    private String sectorTo;
    private Location userLocation;
    private GameMode gameMode = GameMode.SURVIVAL;
    private int selectedItemSlot = 0;
    private int xpTotal = 0;
    private int foodLevel = 0;
    private int xpLevel = 0;
    private float xpAmount = 0;
    private double playerHealth = 20;
    private boolean isFlying = false;
    private long lastGuildCreate = -1L;
    private long turboDropReceiveTime = 0L;
    private long lastResetPointsTime = 0L;
    private long lastJoin = System.currentTimeMillis();
    private Collection<PotionEffect> potionEffects = new HashSet<>();

    public User() {
        this.uuid = null;
        this.userName = null;
    }

    public User(final UUID uuid, final String userName, final Sector sector) {
        this.uuid = uuid;
        this.userName = userName;
        this.sector = sector;
        this.enderChests.putIfAbsent(EnderType.DEFAULT, new EnderChestImpl());
        this.enderChests.putIfAbsent(EnderType.PREMIUM, new EnderChestImpl());
    }

    public User(final Player p, final Sector sector) {
        this.uuid = p.getUniqueId();
        this.userName = p.getName();
        this.sector = sector;
        this.inventory = p.getInventory().getContents();
        this.armorInventory = p.getInventory().getArmorContents();
        this.selectedItemSlot = p.getInventory().getHeldItemSlot();
        this.xpTotal = p.getTotalExperience();
        this.xpAmount = p.getExp();
        this.xpLevel = p.getLevel();
        this.foodLevel = p.getFoodLevel();
        this.playerHealth = p.getHealth();
        this.isFlying = p.isFlying();
        this.enderChests.putIfAbsent(EnderType.DEFAULT, new EnderChestImpl());
        this.enderChests.putIfAbsent(EnderType.PREMIUM, new EnderChestImpl());
    }

    public boolean saveUserToValues() {
        this.enderChests.forEach((type, ender) -> {
            ender.save();
        });
        final Player p = Bukkit.getPlayer(this.uuid);
        if (p == null || !p.isOnline()) {
            return false;
        }
        this.inventory = p.getInventory().getContents();
        this.armorInventory = p.getInventory().getArmorContents();
        this.userLocation = p.getLocation();
        this.selectedItemSlot = p.getInventory().getHeldItemSlot();
        this.xpTotal = p.getTotalExperience();
        this.xpAmount = p.getExp();
        this.xpLevel = p.getLevel();
        this.foodLevel = p.getFoodLevel();
        this.playerHealth = p.getHealth();
        this.potionEffects = p.getActivePotionEffects();
        this.isFlying = p.isFlying();
        this.gameMode = p.getGameMode();
        return true;
    }
    public boolean saveUserToValues(Location location) {
        this.enderChests.forEach((type, ender) -> {
            ender.save();
        });
        final Player p = Bukkit.getPlayer(this.uuid);
        if (p == null || !p.isOnline()) {
            return false;
        }
        this.inventory = p.getInventory().getContents();
        this.armorInventory = p.getInventory().getArmorContents();
        this.userLocation = location;
        this.selectedItemSlot = p.getInventory().getHeldItemSlot();
        this.xpTotal = p.getTotalExperience();
        this.xpAmount = p.getExp();
        this.xpLevel = p.getLevel();
        this.foodLevel = p.getFoodLevel();
        this.playerHealth = p.getHealth();
        this.potionEffects = p.getActivePotionEffects();
        this.isFlying = p.isFlying();
        this.gameMode = p.getGameMode();
        return true;
    }
    public void applyValuesToPlayer() {
        final Player p = Bukkit.getPlayer(this.uuid);
        this.enderChests.forEach((type, ender) -> {
            ender.setup();
        });
        if (p != null && p.isOnline()) {
            p.getInventory().setContents(this.inventory);
            p.getInventory().setArmorContents(this.armorInventory);
            p.teleport(this.userLocation);
            p.getInventory().setHeldItemSlot(this.selectedItemSlot);
            p.setTotalExperience(this.xpTotal);
            p.setLevel(this.xpLevel);
            p.setExp(this.xpAmount);
            p.setFoodLevel(this.foodLevel);
            p.setHealth(this.playerHealth);
            p.setGameMode(this.gameMode);
            p.setAllowFlight(this.isFlying);
            //p.setFlying(this.isFlying);
            p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            for (PotionEffect potionEffect : this.potionEffects) {
                p.addPotionEffect(potionEffect);
            }
        }
    }


    @Override
    public void insert(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.USERS.putAsync(this.uuid, GsonUtil.toJson(this));
        } else {
            RedisChannel.INSTANCE.USERS.put(this.uuid, GsonUtil.toJson(this));
        }
    }

    @Override
    public void delete(boolean async) {
        if (async) {
            RedisChannel.INSTANCE.USERS.removeAsync(this.uuid);
        } else {
            RedisChannel.INSTANCE.USERS.remove(this.uuid);
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getName() {
        return userName;
    }

    public EntityPlayer asEntityPlayer() {
        final Player p = asPlayer();
        if (p == null) {
            return null;
        }
        return ((CraftPlayer) p).getHandle();
    }

    public void setUserSector(final Sector sector) {
        this.sector = sector;
    }

    public Sector getSector() {
        return sector;
    }

    public boolean isOnline() {
        return getSector().isSectorOnline() && getSector().getOnlinePlayers().contains(getName());
    }

    public long getTurboDropTime() {
        return turboDropTime;
    }

    public long getTurboExpTime() {
        return turboExpTime;
    }

    public void setTurboDropTime(long turboDropTime) {
        this.turboDropTime = turboDropTime;
    }

    public void setTurboExpTime(long turboExpTime) {
        this.turboExpTime = turboExpTime;
    }

    public Player asPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public long getLastResetPointsTime() {
        return lastResetPointsTime;
    }

    public void setLastResetPointsTime(long lastResetPointsTime) {
        this.lastResetPointsTime = lastResetPointsTime;
    }

    public long getTurboDropReceiveTime() {
        long time = TimeUtil.parseDateDiff((userGroup.getGroupLevel() > UserGroup.HELPER.getGroupLevel() ? "1m" : userGroup == UserGroup.ENIU ? "4h" : userGroup == UserGroup.SPONSOR ? "8h" : userGroup == UserGroup.SVIP ? "24h" : "48h"),true) - System.currentTimeMillis();
        return turboDropReceiveTime +  time;
    }

    public void setTurboDropReceiveTime(long turboDropReceiveTime) {
        this.turboDropReceiveTime = turboDropReceiveTime;
    }
    public boolean isTurboDropReceiveTime(){
        long time = TimeUtil.parseDateDiff((userGroup.getGroupLevel() > UserGroup.HELPER.getGroupLevel() ? "1m" : userGroup == UserGroup.ENIU ? "4h" : userGroup == UserGroup.SPONSOR ? "8h" : userGroup == UserGroup.SVIP ? "24h" : "48h"),true) - System.currentTimeMillis();
        return turboDropReceiveTime + time <= System.currentTimeMillis();
    }

    public Collection<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public void setPotionEffects(Collection<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    public List<String> getAchievements() {
        return achievements;
    }
    public String getAchievementsAsString(){
        return StringUtils.join(getAchievements(),",");
    }
    public void setAchievementsFromString(String achievements){
        for(String a : achievements.split(",")){
            getAchievements().add(a);
        }
    }

    public List<String> getQuickSell() {
        return quickSell;
    }

    public String getQuickSellAsString(){
        return StringUtils.join(getQuickSell(),",");
    }
    public void setQuickSellFromString(String quickSell){
        for(String a : quickSell.split(",")){
            getQuickSell().add(a);
        }
    }

    public void setQuickSell(List<String> quickSell) {
        this.quickSell = quickSell;
    }


    public UserChat getChat() {
        return userChat;
    }


    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }


    public int getKills() {
        return kills;
    }

    public long getLastKillTime() {
        return lastKillTime;
    }

    public String getLastKill() {
        return lastKill;
    }

    public void setLastKill(String lastKill) {
        this.lastKill = lastKill;
    }

    public void setLastKillTime(long lastKillTime) {
        this.lastKillTime = lastKillTime;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getLogouts() {
        return logouts;
    }

    public void setLogouts(int logouts) {
        this.logouts = logouts;
    }

    public long getTimePlay() {
        return timePlay;
    }

    public void setTimePlay(long timePlay) {
        this.timePlay = timePlay;
    }

    public void addTimePlay(long time){
        this.timePlay += time;
    }

    public void addDepositPearl(int amount) {
        this.depositPearl += amount;
    }

    public void addDepositKox(int amount) {
        this.depositKox += amount;
    }

    public void addDepositRef(int amount) {
        this.depositRef += amount;
    }

    public void addDepositThrowTNT(int amount) {
        this.depositThrowTNT += amount;
    }

    public void addDepositFishingRod(int amount) {
        this.depositFishingRod += amount;
    }

    public void addDepositBlockEmeralds(int amount) {
        this.depositEmeraldBlocks += amount;
    }

    public void removeDepositKox(int amount) {
        this.depositKox -= amount;
    }

    public void removeDepositRef(int amount) {
        this.depositRef -= amount;
    }

    public void removeDepositPearl(int amount) {
        this.depositPearl -= amount;
    }

    public void removeDepositThrowTNT(int amount) {
        this.depositThrowTNT -= amount;
    }

    public void removeDepositFishingRod(int amount) {
        this.depositFishingRod -= amount;
    }

    public void removeDepositBlockEmeralds(int amount) {
        this.depositEmeraldBlocks -= amount;
    }

    public int getEatKox() {
        return eatKox;
    }

    public void setEatKox(int eatKox) {
        this.eatKox = eatKox;
    }

    public int getEatRef() {
        return eatRef;
    }

    public void setEatRef(int eatRef) {
        this.eatRef = eatRef;
    }

    public long getLastJoin() {
        return lastJoin;
    }

    public void setLastJoin(long lastJoin) {
        this.lastJoin = lastJoin;
    }

    public int getThrowPearl() {
        return throwPearl;
    }

    public void setThrowPearl(int throwPearl) {
        this.throwPearl = throwPearl;
    }

    public void addThrowPearl(final int pearls) {
        this.throwPearl += pearls;
    }

    public int getOpenedCase() {
        return openedCase;
    }

    public void setOpenedCase(int openedCase) {
        this.openedCase = openedCase;
    }

    public int getMinedStone() {
        return minedStone;
    }

    public void setMinedStone(int minedStone) {
        this.minedStone = minedStone;
    }

    public int getSpendMoney() {
        return spendMoney;
    }

    public void setSpendMoney(int spendMoney) {
        this.spendMoney = spendMoney;
    }

    public int getPlayerExp() {
        return playerExp;
    }

    public void setPlayerExp(int playerExp) {
        this.playerExp = playerExp;
    }

    public UserGroup getGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public int getPoints() {
        return points;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(final int points) {
        this.points += points;
    }

    public void addDeaths(final int deaths) {
        //this.addedDeaths += deaths;
        this.deaths += deaths;
    }

    public void addKills(final int kills) {
        //this.addedKills += kills;
        this.kills += kills;
    }

    public void addEatKox(final int amount) {
        //this.addedEatKox += amount;
        this.eatKox += amount;
    }

    public void addEatRefil(final int amount) {
        //this.addedEatRef += amount;
        this.eatRef += amount;
    }

    public void addOpenedCase(final int amount) {
        this.openedCase += amount;
    }

    public void addMinedStone(final int amount) {
        this.minedStone += amount;
    }

    public void addSpendMoney(final int amount) {
        this.spendMoney += amount;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public void addCoins(double coins) {
        this.coins += coins;
    }

    public Home getHome(final String homeName) {
        return this.homes.get(homeName);
    }

    public void registerHome(final HomeImpl home) {
        this.homes.put(home.getName(), home);
    }

    public void deleteHome(final String homeName) {
        this.homes.remove(homeName);
    }

    public void setProtection(long protection) {
        this.protection = protection;
    }

    public long getProtection() {
        return protection;
    }
    public void removePoints(int points){
        this.points -= points;
    }

    public boolean isProtection() {
        return getProtection() >= System.currentTimeMillis();
    }

    public int getHomesSize() {
        return this.homes.size();
    }

    public Set<String> getHomeNames() {
        return this.homes.keySet();
    }

    public void setDisableDrops(String disableDrops) {
        String[] array = disableDrops.split(";");
        this.disableDrops.addAll(Arrays.asList(array));
    }

    public String getDisableDrops() {
        return StringUtils.join(disableDrops,";");
    }
    public String getKitTimesAsString(){
        StringBuilder times = new StringBuilder();
        for(Long l : getKitTimes()){
            times.append(l).append(";");
        }
        return times.toString();
    }

    public void setKitTimes(List<Long> kitTimes) {
        this.kitTimes = kitTimes;
    }
    public void setKitTimesAsString(String times){
        kitTimes = new ArrayList<>();
        for(String s : times.split(";")){
            this.kitTimes.add(Long.valueOf(s));
        }
    }

    public List<Long> getKitTimes() {
        return kitTimes;
    }

    public Set<UUID> getIgnoredUsers() {
        return ignoredUsers;
    }

    public boolean isIgnoring(final UUID uuid) {
        return this.ignoredUsers.contains(uuid);
    }

    public boolean isDisabled(Drop drop) {
        return this.disableDrops.contains(drop.getName().toUpperCase());
    }

    public boolean isDisableCobblestone() {
        return disableDrops.contains("cobble");
    }

    public void disableCobblestone() {
        disableDrops.add("cobble");
    }

    public void enableCobblestone() {
        disableDrops.remove("cobble");
    }

    public boolean toggleCobblestone() {
        if (disableDrops.contains("cobble")) {
            enableCobblestone();
            return false;
        }
        disableCobblestone();
        return true;
    }

    public double getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public double getExpToLevel() {
        return level * 162.2;
    }

    public void removeExp(double exp) {
        this.exp -= exp;
    }

    public void addExp(double exp) {
        this.exp += exp;
    }

    public void addLevel(int level) {
        this.level += level;
    }

    public void addCobbleStone(int cobble) {
        this.minedStone += cobble;
    }

    public void addDisableDrop(Drop drop) {
        this.disableDrops.add(drop.getName().toUpperCase());
    }

    public void removeDisableDrop(Drop drop) {
        this.disableDrops.remove(drop.getName().toUpperCase());
    }

    public void setDepositKox(int depositKox) {
        this.depositKox = depositKox;
    }

    public void setDepositPearl(int depositPearl) {
        this.depositPearl = depositPearl;
    }

    public void setDepositRef(int depositRef) {
        this.depositRef = depositRef;
    }

    public int getDepositKox() {
        return depositKox;
    }

    public int getDepositRef() {
        return depositRef;
    }

    public int getDepositPearl() {
        return depositPearl;
    }

    public boolean isFlying() {
        return isFlying;
    }

    public int getDepositSnowBalls() {
        return depositSnowBalls;
    }

    public void setDepositSnowBalls(int depositSnowBalls) {
        this.depositSnowBalls = depositSnowBalls;
    }

    public void addSnowBalls(int amount) {
        this.depositSnowBalls += amount;
    }
    public void removeSnowBalls(int amount){
        this.depositSnowBalls -=  amount;
    }

    public int getDepositEmeraldBlocks() {
        return depositEmeraldBlocks;
    }

    public int getDepositFishingRod() {
        return depositFishingRod;
    }

    public int getDepositThrowTNT() {
        return depositThrowTNT;
    }

    public void removeCoins(double coins) {
        this.coins -= coins;
    }

    public double getKd() {
        if (getKills() == 0 && getDeaths() == 0) {
            return 0;
        } else if (getKills() > 0 && getDeaths() == 0) {
            return getKills();
        } else if (getDeaths() > 0 && getKills() == 0) {
            return -getDeaths();
        } else {
            return MathUtil.round(getKills() / (double) getDeaths(), 2);
        }
    }
    public void addAssist(int assists){
        this.assists += assists;
    }

    public boolean isTurboDrop() {
        return this.turboDropTime >= System.currentTimeMillis();
    }

    public boolean isTurboExp() {
        return this.turboExpTime >= System.currentTimeMillis();
    }


    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Map<EnderType, EnderChestImpl> getEnderChests() {
        return enderChests;
    }

   /* public Incognito getIncognito() {
        return incognito;
    }*/

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public void sendMessage(final String message) {
        final Packet packet = new MessageToUserPacket(this.uuid, message);
        SectorPlugin.getInstance().getSectorClient().sendPacket(packet, this.sector);
    }

    public long getLastGuildCreate() {
        return lastGuildCreate;
    }

    public void setLastGuildCreate(long lastGuildCreate) {
        this.lastGuildCreate = lastGuildCreate;
    }

    public boolean hasPermission(final UserPermission permission) {
        return this.userPermissions.contains(permission);
    }

    public void addPermission(final UserPermission permission) {
        this.userPermissions.add(permission);
    }

    public void removePermission(final UserPermission permission) {
        this.userPermissions.remove(permission);
    }

    public void registerTpaRequest(final String userName) {
        this.tpaRequests.put(userName, System.currentTimeMillis());
    }

    public boolean hasTpaRequestFrom(final String userName) {
        return this.tpaRequests.getIfPresent(userName) != null;
    }

    public void removeTpaRequest(final String userName) {
        this.tpaRequests.invalidate(userName);
    }

    public Cache<String, Long> getTpaRequests() {
        return tpaRequests;
    }
}
