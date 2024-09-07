package pl.supereasy.sectors.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.api.packets.impl.guild.GuildSetBaseLocationPacket;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildSetBaseCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildSetBaseCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final Guild guild = this.plugin.getGuildManager().getGuild(p.getUniqueId());
        if (guild == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (!guild.isOwner(p.getUniqueId())) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUARENOTGUILDOWNER);
        }
        if (!guild.getGuildRegion().isInside(p.getLocation())) {
            return ChatUtil.sendMessage(commandSender, " &8» &cNie mozesz ustawic lokalizacji poza terenem gildii!");
        }
        guild.setHomeLocation(new Location(Bukkit.getWorld("world"), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()));
        guild.insert(true);
        final GuildSetBaseLocationPacket baseLocationPacket = new GuildSetBaseLocationPacket(p.getDisplayName(), guild.getTag(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        this.plugin.getSectorClient().sendGlobalPacket(baseLocationPacket);
        return false;
    }
}
