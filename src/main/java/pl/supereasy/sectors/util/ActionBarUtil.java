package pl.supereasy.sectors.util;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.sectors.data.Sector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarUtil {

    private static String nmsver = Bukkit.getServer().getClass().getPackage().getName();
    private static SectorPlugin plugin;
    public ActionBarUtil(SectorPlugin plugin){
        ActionBarUtil.plugin = plugin;
    }
    static {
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
    }

    public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}


    public static void sendActionBar(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }
        // Call the event, if cancelled don't send Action Bar

        if (nmsver.startsWith("v1_12_")) {
            sendActionBarPost112(player, message);
        } else {
            sendActionBarPre112(player, message);
        }
    }

    private static void sendActionBarPost112(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
            Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
            Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sendActionBarPre112(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        try {
            String mess = message.replace("_", " ");
            String s = ChatColor.translateAlternateColorCodes('&', mess);
            IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + s + "\"}");
            PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(bar);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendActionBar(final Player player, final String message, int duration) {
        sendActionBar(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            BukkitTask bukkitTask = new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, "");
                }
            }.runTaskLater(plugin, duration + 1);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar(player, message);
                }
            }.runTaskLater(plugin, duration);
        }
    }

    public static void sendActionBarToAllPlayers(String message) {
        sendActionBarToAllPlayers(message, -1);
    }

    public static void sendActionBarToAllPlayers(String message, int duration) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendActionBar(p, message, duration);
        }
    }

}
