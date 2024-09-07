package pl.supereasy.sectors.guilds.log.subloggers;

import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.api.Packet;
import pl.supereasy.sectors.api.packets.impl.guild.GuildLogPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.guilds.log.api.LoggerEvent;
import pl.supereasy.sectors.guilds.log.enums.GuildLogType;
import pl.supereasy.sectors.util.ChatUtil;
import pl.supereasy.sectors.util.TimeUtil;

import java.util.List;

public class GuildLeaveLogger<T extends String> implements LoggerEvent<T> {

    @Override
    public void update(User user, T type) {
        final List<String> msg = GuildConfig.INSTANCE.GUI_ITEM_LOGACTION_LEAVE;
        for (int i = 0; i < msg.size(); i++) {
            String s = msg.get(i);
            s = s.replace("{DATE}", TimeUtil.getDate(System.currentTimeMillis()));
            s = s.replace("{PLAYER}", user.getName());
            s = s.replace("{CATEGORY}", GuildLogType.LEAVE.name());
            msg.set(i, s);
        }
        final Packet packet = new GuildLogPacket(type, user.getName(), ChatUtil.fixColor(msg), GuildLogType.LEAVE);
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
    }
}
