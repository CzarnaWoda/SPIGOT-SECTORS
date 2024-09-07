package pl.supereasy.sectors.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.core.combat.api.CombatManager;
import pl.supereasy.sectors.core.combat.data.Combat;

import java.util.ArrayList;
import java.util.List;

public class LocationUtil
{
  public static Location convertStringToBlockLocation(final String string)
  {
    if (string != null)
    {
      final String[] wxyz = string.split(",");
      final World w = Bukkit.getWorld(wxyz[0]);
      final int x = Integer.parseInt(wxyz[1]);
      final int y = Integer.parseInt(wxyz[2]);
      final int z = Integer.parseInt(wxyz[3]);
      return new Location(w, x, y, z);
    }
    return null;
  }
  public static Location getCenter(Location loc) {
    return new Location(loc.getWorld(),
            getRelativeCoord(loc.getBlockX()),
            getRelativeCoord(loc.getBlockY()),
            getRelativeCoord(loc.getBlockZ()));
  }

  private static double getRelativeCoord(int i) {
    double d = i;
    d = d < 0 ? d - .5 : d + .5;
    return d;
  }
  public static Location convertStringToLocation(final String string)
  {
    if (string == null) {
      return null;
    }
    final String[] wxyzPitchYaw = string.split(",");
    final World w = Bukkit.getWorld(wxyzPitchYaw[0]);
    final double x = Double.parseDouble(wxyzPitchYaw[1]);
    final double y = Double.parseDouble(wxyzPitchYaw[2]);
    final double z = Double.parseDouble(wxyzPitchYaw[3]);
    final int pitch = Integer.parseInt(wxyzPitchYaw[4]);
    final int yaw = Integer.parseInt(wxyzPitchYaw[5]);
    final Location location = new Location(w, x, y, z);
    location.setPitch(pitch);
    location.setYaw(yaw);
    return location;
  }
  
  public static String convertLocationBlockToString(final Location location)
  {
    String world = location.getWorld().getName();
    int x = location.getBlockX();
    int y = location.getBlockY();
    int z = location.getBlockZ();
    return world + "," + x + "," + y + "," + z;
  }
  
  public static String convertLocationToString(final Location location)
  {
	final String world = location.getWorld().getName();
	final double x = location.getX();
	final double y = location.getY();
	final double z = location.getZ();
	final int pitch = (int)location.getPitch();
	final int yaw = (int)location.getYaw();
    return world + "," + x + "," + y + "," + z + "," + pitch + "," + yaw;
  }
  public static List<Player> getPlayersInRadius(Location location, int distance) {
    List<Player> players = new ArrayList<>();
    for(Player all : location.getWorld().getPlayers()) {
      if(location.distance(all.getLocation()) <= distance) {
        if(all.getLocation().getBlock().getType() == Material.STONE_PLATE || all.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STONE_PLATE) {
          final Combat c = SectorPlugin.getInstance().getCombatManager().getCombat(all);
          if(c != null && !c.hasFight()) {
            players.add(all);
          }
        }
      }
    }
    return players;
  }
  public static List<Player> getPlayersInRadiusNoStonePlate(Location location, int distance) {
    List<Player> players = new ArrayList<>();
    for(Player all : location.getWorld().getPlayers()) {
      if(location.distance(all.getLocation()) <= distance) {
          players.add(all);
      }
    }
    return players;
  }
}
