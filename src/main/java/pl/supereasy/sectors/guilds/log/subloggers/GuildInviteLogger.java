package pl.supereasy.sectors.guilds.log.subloggers;

import org.bukkit.Bukkit;
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

public class GuildInviteLogger<T extends String> implements LoggerEvent<T> {

    @Override
    public void update(User user, String invitedName) {
        final List<String> msg = GuildConfig.INSTANCE.GUI_ITEM_LOGACTION_INVITE;
        for (int i = 0; i < msg.size(); i++) {
            String s = msg.get(i);
            s = s.replace("{DATE}", TimeUtil.getDate(System.currentTimeMillis()));
            s = s.replace("{PLAYER}", user.getName());
            s = s.replace("{CATEGORY}", GuildLogType.GUILD_INVITE.name());
            s = s.replace("{TARGETPLAYER}", invitedName);
            msg.set(i, s);
        }
        final Packet packet = new GuildLogPacket(user.getGuild().getTag(), invitedName, ChatUtil.fixColor(msg), GuildLogType.GUILD_INVITE);
        SectorPlugin.getInstance().getSectorClient().sendGlobalPacket(packet);
    }
}
