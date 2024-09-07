package pl.supereasy.sectors.api.sectors.managers;

import com.google.common.cache.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.synchro.UserChangeSectorPacket;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SectorManager {

    private final SectorPlugin sectorPlugin;
    private final Cache<UUID, Long> changeCache;
    private final Cache<UUID, Long> lastSectorAction;
    private final Cache<UUID, Sector> sectorRequestAccepted;
    private final Map<Integer, Sector> sectors;
    private final Map<String, Sector> stringSectors;
    private Sector currentSector;


    public SectorManager(SectorPlugin plugin) {
        sectorPlugin = plugin;
        this.changeCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
        this.lastSectorAction = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.SECONDS).build();
        this.sectorRequestAccepted = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).removalListener((RemovalListener<UUID, Sector>) removalNotification -> {
            if (removalNotification.getCause() == RemovalCause.EXPIRED) {
                final Player p = Bukkit.getPlayer(removalNotification.getKey());
                if (p != null && p.isOnline()) {
                    ChatUtil.sendMessage(p, "&cSektor na ktory probujesz sie polaczyc przekroczyl limit czasu polaczenia!");
                }
            }
        }).build();
        this.sectors = new HashMap<>();
        this.stringSectors = new HashMap<>();
    }

    public void sendToSector(User user, Sector sector) {
        if (user.saveUserToValues()) {
            this.sectorRequestAccepted.put(user.getUUID(), sector);
            final Packet packet = new UserChangeSectorPacket(user);
            this.sectorPlugin.getSectorClient().sendUser(packet, sector.getSectorName());
            final Player p = user.asPlayer();
            if (p != null && p.isOnline()) {
                ChatUtil.sendMessage(p, "&7Wyslano zapytanie do sektora &8(&a" + sector.getSectorName() + "&8)");
            }
        } else {
            final Player p = user.asPlayer();
            if (p != null && p.isOnline()) {
                ChatUtil.sendMessage(p, "&cWystapil blad z Twoim profilem, prosimy relgonac!");
            }
        }
    }

    public void handleSectorConnect(final UUID uuid) {
        final Player p = Bukkit.getPlayer(uuid);
        if (p == null || !p.isOnline()) {
            return;
        }
        Sector sector = this.sectorRequestAccepted.getIfPresent(uuid);
        if (sector == null) {
            ChatUtil.sendMessage(p, "&cWystapil blad z teleportacja. Brak sektora. Zglos to administratorowi!");
            sector = getCurrentSector();
            //TODO CHECK
        }
        if (!sector.isSectorOnline()) {
            ChatUtil.sendMessage(p, "&7Sektor &c" + sector.getSectorName() + " &7jest obecnie offline!");
            return;
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DataOutputStream dos = new DataOutputStream(bos);
        try {
            this.sectorRequestAccepted.invalidate(uuid);
            dos.writeUTF("Connect");
            dos.writeUTF(sector.getSectorName());
            p.sendPluginMessage(this.sectorPlugin, "BungeeCord", bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean canChangeSector(final UUID uuid) {
        return this.lastSectorAction.getIfPresent(uuid) == null;
    }

    public boolean canMoveToAnotherSector(final UUID uuid) {
        return this.changeCache.getIfPresent(uuid) != null;
    }

    public void registerSectorChange(final UUID uuid) {
        this.changeCache.put(uuid, System.currentTimeMillis());
    }
    public void registerSectorAction(final  UUID uuid){ this.lastSectorAction.put(uuid, System.currentTimeMillis());}

    public double howCloseToOtherSector(Location location){
        for(Sector sector : sectors.values()){
            if(sector.isNearBorder(location, 20)){
                return sector.howCloseBorder(location);
            }
        }
        return Double.MAX_VALUE;
    }
    public double howCloseToOtherSector(Location location, int additional){
        for(Sector sector : sectors.values()){
            if(sector.isNearBorder(location, additional)){
                return sector.howCloseBorder(location);
            }
        }
        return Double.MAX_VALUE;
    }
    public boolean isInSector(Location location){
        for(Sector sector : sectors.values()){
            if(sector.entityInSector(location) && (sector.getUniqueSectorID() == sectorPlugin.getCurrentSectorConfig().getSectorUniqueID())){
                return true;
            }
        }
        return false;
    }
    public boolean isInSector(int x, int z){
        for(Sector sector : sectors.values()){
            if(sector.smthInSector(x,z) && (sector.getUniqueSectorID() == sectorPlugin.getCurrentSectorConfig().getSectorUniqueID())){
                return true;
            }
        }
        return false;
    }
    public boolean isNear(Location location){
        for(Sector sector : sectors.values()){
            if(sector.isNearBorder(location,20))
                return true;
        }
        return false;
    }
    public boolean isNear(Location location, int additional){
        for(Sector sector : sectors.values()){
            if(sector.isNearBorder(location,additional))
                return true;
        }
        return false;
    }
    public Sector getActuallySector(Location location) {
        for (Sector sector : sectors.values()) {
            if (sector.entityInSector(location) && (sector.getUniqueSectorID() == sectorPlugin.getCurrentSectorConfig().getSectorUniqueID())) {
                return sector;
            }
        }
        return null;
    }

    public Sector getSectorAt(Location location) {
        for (Sector sector : sectors.values()) {
            if (sector.entityInSector(location) && sector.getUniqueSectorID() != sectorPlugin.getCurrentSectorConfig().getSectorUniqueID()) {
                return sector;
            }
        }
        return null;
    }
    public Sector getSectorAtWithOffSet(Location location) {
        for (Sector sector : sectors.values()) {
            if (sector.entityInSectorWithOffSet(location) && sector.getUniqueSectorID() != sectorPlugin.getCurrentSectorConfig().getSectorUniqueID()) {
                return sector;
            }
        }
        return null;
    }

    public Sector getSector(final String sectorName) {
        return this.stringSectors.get(sectorName);
    }

    public Sector getCurrentSector() {
        if (this.currentSector == null) {
            this.currentSector = this.sectors.get(this.sectorPlugin.getCurrentSectorConfig().getSectorUniqueID());
            return this.currentSector;
        }
        return this.currentSector;
    }

    public Sector getSector(final int sectorID) {
        return this.sectors.get(sectorID);
    }

    public Map<Integer, Sector> getSectors() {
        return sectors;
    }


    public Map<String, Sector> getStringSectors() {
        return stringSectors;
    }

    public Cache<UUID, Long> getChangeCache() {
        return changeCache;
    }

    public void registerSector(final Sector sector) {
        this.sectors.put(sector.getUniqueSectorID(), sector);
        this.stringSectors.put(sector.getSectorName(), sector);
    }
}
