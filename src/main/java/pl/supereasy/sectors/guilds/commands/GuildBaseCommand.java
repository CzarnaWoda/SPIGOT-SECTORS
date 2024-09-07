package pl.supereasy.sectors.guilds.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.Guild;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildBaseCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildBaseCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup,aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        final Guild guild = user.getGuild();
        if(guild == null){
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if(guild.getHomeLocation() == null){
            this.plugin.getTeleportManager().teleportPlayer(user, guild.getGuildRegion().getCenter(), guild.getGuildSector());
            return ChatUtil.sendMessage(commandSender, "Nie masz bazy jestes tp na srodek terenu");
        }
        this.plugin.getTeleportManager().teleportPlayer(user, guild.getHomeLocation(), guild.getGuildSector());
        return false;
    }
}
