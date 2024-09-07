package pl.supereasy.sectors.core.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;
import pl.supereasy.sectors.api.sectors.type.SectorType;
import pl.supereasy.sectors.core.combat.data.Combat;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.HashMap;
import java.util.UUID;

public class MoveListener implements Listener {

    private final SectorPlugin plugin;
    private final HashMap<Player, Long> times;
    private final UUID leaveUUID;
    private final UUID enterUUID;
    private final UUID intruderUUID;

    public MoveListener(SectorPlugin plugin) {
        this.plugin = plugin;
        this.times = new HashMap<>();
        this.enterUUID = UUID.randomUUID();
        this.leaveUUID = UUID.randomUUID();
        this.intruderUUID = UUID.randomUUID();
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        final int xto = e.getTo().getBlockX();
        final int zto = e.getTo().getBlockZ();
        final Player player = e.getPlayer();
        if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            if (Math.abs(xto) + 10 >= plugin.getCoreConfig().BORDERMANAGER_BORDER || Math.abs(zto) + 10 >= plugin.getCoreConfig().BORDERMANAGER_BORDER) {
                if (times.get(player) != null && times.get(player) <= System.currentTimeMillis()) {
                    ChatUtil.sendMessage(e.getPlayer(), "&4Blad: &cOsiagnales granice swiata!");
                    times.put(player, System.currentTimeMillis() + 1000L);
                }
                e.setCancelled(true);
                return;
            }
            final Sector sector = plugin.getSectorManager().getCurrentSector();
            final Sector sectorTo = plugin.getSectorManager().getSectorAt(e.getTo());
            if (sector != null && sectorTo != null && sectorTo != sector) {
                final Long time = times.get(player);
                if (time != null && time <= System.currentTimeMillis()) {
                    ChatUtil.sendMessage(e.getPlayer(), "&4Blad: &cNie mozesz tego zrobic!");
                    times.put(player, System.currentTimeMillis() + 1000L);
                }
                e.setCancelled(true);

            }
        }
    }

    @EventHandler
    public void onTeleport(final PlayerMoveEvent event) {
        if ((event.getFrom().getBlockX() == event.getTo().getBlockX()) && (event.getFrom().getBlockY() == event.getTo().getBlockY()) && (event.getFrom().getBlockZ() == event.getTo().getBlockZ())) {
            return;
        }
        this.plugin.getTeleportManager().deleteRequestIfExists(event.getPlayer());
    }

    @EventHandler
    public void onMovePlayer(final PlayerMoveEvent e){
        if(plugin.getSectorManager().getCurrentSector().getSectorType().equals(SectorType.SPAWN)){
            final Player player = e.getPlayer();
            final int xto = e.getTo().getBlockX();
            final int zto = e.getTo().getBlockZ();
            final Combat c = plugin.getCombatManager().getCombat(player);
            if(c != null && c.hasFight()){
                final ProtectedRegion protectedRegion = plugin.getWorldGuard().getRegionManager(Bukkit.getWorlds().get(0)).getRegion("spawn");
                if(protectedRegion != null && protectedRegion.contains(xto,60,zto)){
                    Vector playerCenterVector = Bukkit.getWorld("world").getSpawnLocation().toVector();
                    Vector playerToThrowVector = player.getEyeLocation().toVector();

                    double x = playerToThrowVector.getX() - playerCenterVector.getX();
                    double z = playerToThrowVector.getZ() - playerCenterVector.getZ();

                    Vector throwVector = new Vector(x, 1, z).normalize().multiply(2);
                    throwVector.multiply(0.41);
                    throwVector.setY(0);
                    player.setVelocity(throwVector);
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final int xfrom = e.getFrom().getBlockX();
        final int zfrom = e.getFrom().getBlockZ();
        final int yfrom = e.getFrom().getBlockY();
        final int xto = e.getTo().getBlockX();
        final int yto = e.getTo().getBlockY();
        final int zto = e.getTo().getBlockZ();
        if (xfrom != xto || zfrom != zto || yfrom != yto) {

            final Location to = e.getTo();

            final Sector sectorTo = plugin.getSectorManager().getSectorAtWithOffSet(to);


            if (sectorTo == null) {
                final Player player = e.getPlayer();
                if (Math.abs(xto) > plugin.getCoreConfig().BORDERMANAGER_BORDER - 10 || Math.abs(zto) > plugin.getCoreConfig().BORDERMANAGER_BORDER - 10) {
                    if (times.get(player) != null && times.get(player) <= System.currentTimeMillis()) {
                        ChatUtil.sendMessage(e.getPlayer(), "&4Blad: &cOsiagnales granice swiata!");
                    }
                    times.put(player, System.currentTimeMillis() + 1000L);
                    e.getPlayer().teleport(e.getFrom());
                    Vector vector = e.getFrom().toVector().subtract(to.toVector());
                    vector.setY(0.2D);
                    e.getPlayer().setVelocity(vector.multiply(4));
                    e.setCancelled(true);
                }
                return;
            }
            final User user = this.plugin.getUserManager().getUser(e.getPlayer().getUniqueId());
            if (!user.getGroup().hasPermission(UserGroup.HELPER) && this.plugin.getSectorManager().canMoveToAnotherSector(e.getPlayer().getUniqueId())) {
                e.getPlayer().teleport(e.getFrom());
                Vector vector = e.getFrom().toVector().subtract(to.toVector());
                vector.setY(0.2D);
                e.getPlayer().setVelocity(vector.multiply(4));
                e.getPlayer().sendMessage(ChatUtil.fixColor("&4Blad: &cZmieniales ostatnio sektor!"));
                e.setCancelled(true);
                return;
            }

            if (!sectorTo.isSectorOnline()) {
                ChatUtil.sendMessage(e.getPlayer(), "&4Blad: &7Sektor &8(&a" + sectorTo.getSectorName() + "&8) &7jest obecnie &cwylaczony");
                return;
            }
            final Combat combat = this.plugin.getCombatManager().getCombat(e.getPlayer());
            if (combat != null && combat.hasFight()) {
                ChatUtil.sendMessage(e.getPlayer(), "&4Blad: &cNie mozesz przechodzic przez sektor podczas pvp!");
                e.getPlayer().teleport(e.getFrom());
                Vector vector = e.getFrom().toVector().subtract(to.toVector());
                vector.setY(0.2D);
                e.getPlayer().setVelocity(vector.multiply(1.8));
                e.setCancelled(true);
                return;
            }

            if (plugin.getSectorManager().canChangeSector(e.getPlayer().getUniqueId()) && !e.getPlayer().isInsideVehicle() && !e.getPlayer().isDead()) { //Przenoszenie przez sektor <- canDoAction = canChangeSector
                this.plugin.getSectorManager().sendToSector(user, sectorTo);
                this.plugin.getSectorManager().registerSectorAction(e.getPlayer().getUniqueId());
            }
        }
    }
}
