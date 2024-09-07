package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.guild.GuildResizePacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildEnlargeCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildEnlargeCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final Guild guild = this.plugin.getGuildManager().getGuild(p.getUniqueId());
        if (guild == null) {
            return ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (!guild.isOwner(p.getUniqueId())) {
            return ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUARENOTGUILDOWNER);
        }
        guild.insert(true);
        final GuildResizePacket guildResizePacket = new GuildResizePacket(guild.getTag(), p.getDisplayName(), guild.getGuildRegion().getSize() + 5);
        //TODO Remove items! algorytm? stala wartosc kosztu?
        this.plugin.getSectorClient().sendGlobalPacket(guildResizePacket);
        return false;
    }
}
