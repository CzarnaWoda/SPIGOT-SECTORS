package pl.supereasy.sectors.core.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.util.ChatUtil;

public class VehicleListener implements Listener {

    private final SectorPlugin plugin;
    public VehicleListener(SectorPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent e){
        if(e.getVehicle().getPassenger() instanceof Player) {
            final int xfrom = e.getFrom().getBlockX();
            final int zfrom = e.getFrom().getBlockZ();
            final int yfrom = e.getFrom().getBlockY();
            final int xto = e.getTo().getBlockX();
            final int yto = e.getTo().getBlockY();
            final int zto = e.getTo().getBlockZ();
                if (plugin.getSectorManager().getCurrentSector().isNearBorder(e.getTo(), 20)) {
                    if(e.getVehicle().getPassenger() instanceof Player) {
                        final Player player = (Player) e.getVehicle().getPassenger();
                        e.getVehicle().getPassenger().leaveVehicle();
                        player.teleport(e.getFrom());
                        e.getVehicle().eject();
                        e.getVehicle().remove();
                    }
                }
        }
    }
    @EventHandler
    public void onJoinVehicle(VehicleEnterEvent e){
        if(e.getEntered() instanceof Player) {
            e.setCancelled(true);
            e.getVehicle().remove();
        }
    }
}
