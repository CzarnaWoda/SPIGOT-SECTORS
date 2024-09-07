package pl.supereasy.sectors.guilds.log.subloggers;

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

public class GuildKickLogger<T extends String> implements LoggerEvent<T> {

    @Override
    public void update(User user, String kickedPlayer) {
        final List<String> msg = GuildConfig.INSTANCE.GUI_ITEM_LOGACTION_KICK;
        for (int i = 0; i < msg.size(); i++) {
            String s = msg.get(i);
            s = s.replace("{DATE}", TimeUtil.getDate(System.currentTimeMillis()));
            s = s.replace("{PLAYER}", user.getName());
            s = s.replace("{CATEGORY}", GuildLogType.KICK.name());
            s = s.replace("{TARGETPLAYER}", kickedPlayer);
            msg.set(i, s);
        }
        final Packet packet = new GuildLogPacket(user.getGuild().getTag(), user.getName(), ChatUtil.fixColor(msg), GuildLogType.KICK);
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
    }
}
