package pl.supereasy.sectors.api.blazingpack.cuboids;

import blazingpackserverapi.BlazingPackServerAPI;
import blazingpackserverapi.CustomPayload;
import blazingpackserverapi.MessagedRectangle;
import blazingpackserverapi.Rectangle;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.supereasy.bpaddons.cuboids.CuboidUtil;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.api.GuildManager;
import pl.supereasy.sectors.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class BlazingCuboidAPI {


    public static void reloadCuboids(Player p) {
        if (SectorPlugin.getInstance().getGuildManager().currentSectorGuilds().isEmpty()) {
            return;
        }
        final List<MessagedRectangle> list = new ArrayList<>();

        for (Object obj : SectorPlugin.getInstance().getGuildManager().currentSectorGuilds()) {
            final Guild guild = (Guild) obj;
            if (guild.isMember(p.getUniqueId())) {
                continue;
            }
            list.add(new MessagedRectangle(ChatUtil.fixColor("&4Blad: &cNie mozesz tutaj tego zrobic! To nie Twoja gildia"), guild.getGuildRegion().getCenterX() - guild.getGuildRegion().getSize(), guild.getGuildRegion().getCenterZ() - guild.getGuildRegion().getSize(), guild.getGuildRegion().getSize() * 2 + 1, guild.getGuildRegion().getSize() * 2 + 1, false, true));
        }

        final CustomPayload payload = BlazingPackServerAPI.setAllCompressedCuboids(list, new Rectangle(0, 0, 0, 0, true, true));
        CuboidUtil.sendPayload(p, payload.getChannel(), payload.getData());
    }

    public static void reloadCuboidsForAllPlayers() {
        if (SectorPlugin.getInstance().getGuildManager().currentSectorGuilds().isEmpty()) {
            return;
        }
        final List<MessagedRectangle> list = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            for (Object obj : SectorPlugin.getInstance().getGuildManager().currentSectorGuilds()) {
                final Guild guild = (Guild) obj;
                if (guild.isMember(p.getUniqueId())) {
                    continue;
                }
                list.add(new MessagedRectangle(ChatUtil.fixColor("&4Blad: &cNie mozesz tutaj tego zrobic! To nie Twoja gildia"), guild.getGuildRegion().getCenterX() - guild.getGuildRegion().getSize(), guild.getGuildRegion().getCenterZ() - guild.getGuildRegion().getSize(), guild.getGuildRegion().getSize() * 2 + 1, guild.getGuildRegion().getSize() * 2 + 1, false, true));
            }

            final CustomPayload payload = BlazingPackServerAPI.setAllCompressedCuboids(list, new Rectangle(0, 0, 0, 0, true, true));
            CuboidUtil.sendPayload(p, payload.getChannel(), payload.getData());
        }
    }


    public static void resetCuboids(final Player p) {
        final CustomPayload payload = BlazingPackServerAPI.clearCuboids();
        CuboidUtil.sendPayload(p, payload.getChannel(), payload.getData());
    }
}
