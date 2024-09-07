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
import pl.supereasy.sectors.guilds.enums.GuildPermission;
import pl.supereasy.sectors.guilds.inventories.GuildManageInventory;
import pl.supereasy.sectors.util.ChatUtil;

public class GuildManagerCommand extends CustomCommand {

    private final SectorPlugin plugin;

    public GuildManagerCommand(SectorPlugin plugin, String commandName, UserGroup minGroup, String... aliases) {
        super(plugin, commandName, minGroup, aliases);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player p = (Player) commandSender;
        final User u = this.plugin.getUserManager().getUser(p.getUniqueId());
        if (u.getGuild() == null) {
            return ChatUtil.sendMessage(p, GuildConfig.INSTANCE.MESSAGES_YOUDONTHAVEGUILD);
        }
        if (u.getGuild().isOwner(p.getUniqueId()) || u.getGuild().getMember(p.getUniqueId()).hasPermission(GuildPermission.PANEL_ACCESS)) {
            GuildManageInventory.getGuildManagerGUI(u.getGuild()).openInventory(p);
        } else {
            return ChatUtil.sendMessage(p, "  &8» &cNie posiadasz uprawnien do panelu gildii!");
        }
        return false;
    }
}
