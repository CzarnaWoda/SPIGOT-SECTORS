package pl.supereasy.sectors.guilds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.supereasy.sectors.SectorPlugin;
import pl.supereasy.sectors.config.GuildConfig;
import pl.supereasy.sectors.core.commands.api.CustomCommand;
import pl.supereasy.sectors.core.user.enums.UserGroup;
import pl.supereasy.sectors.core.user.impl.User;
import pl.supereasy.sectors.guilds.inventories.GuildMainTreasureInventory;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildTreasureCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildTreasureCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final User user = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (user.getGuild() == null) {
            return ChatUtil.sendMessage(commandSender, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (user.getGuild().getGuildSector().getUniqueSectorID() != this.plugin.getSectorManager().getCurrentSector().getUniqueSectorID()) {
            return ChatUtil.sendMessage(commandSender, " &8» &cSkarbiec mozesz otwierac tylko na sektorze swojej gildii!");
        }
        GuildMainTreasureInventory.getTreasureMainInventory(user, user.getGuild()).openInventory(p);
        return false;
    }
}
