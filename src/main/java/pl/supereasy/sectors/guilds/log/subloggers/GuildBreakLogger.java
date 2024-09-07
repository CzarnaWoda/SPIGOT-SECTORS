package pl.supereasy.sectors.guilds.log.subloggers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildLogPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.log.api.LoggerEvent;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.List;

public class GuildBreakLogger<T extends Block> implements LoggerEvent<T> {

    @Override
    public void update(User user, Block block) {
        if (block.getType() == Material.BEACON) {
            final List<String> msg = GuildConfig.INSTANCE.GUI_ITEM_LOGACTION_BREAK_BEACON;
            for (int i = 0; i < msg.size(); i++) {
                String s = msg.get(i);
                s = s.replace("{DATE}", TimeUtil.getDate(System.currentTimeMillis()));
                s = s.replace("{PLAYER}", user.getName());
                s = s.replace("{CATEGORY}", GuildLogType.BREAK.name());
                msg.set(i, s);
            }
            final Packet packet = new GuildLogPacket(user.getGuild().getTag(), user.getName(), ChatUtil.fixColor(msg), GuildLogType.BREAK);
            SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
        }
    }
}
